package com.blueveery.jackson.scopes;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 23.09.16.
 */
public class ShortNameIdResolver implements TypeIdResolver {
    private JavaType javaType = null;
    static private Map<String, JavaType> typeIdToJavaTypeMap = new HashMap<>();

    @Override
    public void init(JavaType javaType) {
        this.javaType = javaType;
    }

    @Override
    public String idFromValue(Object object) {
        Class clazz = object.getClass();
        if(object instanceof ProxyObject){
            clazz = object.getClass().getSuperclass();
        }
        String typeId = clazz.getSimpleName().toLowerCase();
        if(!typeIdToJavaTypeMap.containsKey(typeId)){
            JavaType newJavaType = TypeFactory.defaultInstance().constructType(clazz);
            typeIdToJavaTypeMap.put(typeId, newJavaType);
        }
        return typeId;
    }

    @Override
    public String idFromValueAndType(Object object, Class<?> aClass) {
        return idFromValue(object);
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Deprecated
    @Override
    public JavaType typeFromId(String typeId) {
        return typeIdToJavaTypeMap.get(typeId);
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String typeId) {
        String[] idComponents = typeId.split("/");
        return typeIdToJavaTypeMap.get(idComponents[0]);
    }

    @Override
    public String getDescForKnownTypeIds() {
        return "test-test";
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.NAME;
    }
}

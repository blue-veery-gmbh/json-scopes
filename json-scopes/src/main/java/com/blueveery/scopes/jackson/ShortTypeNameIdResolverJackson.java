package com.blueveery.scopes.jackson;

import com.blueveery.scopes.ShortTypeNameIdResolver;
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
public class ShortTypeNameIdResolverJackson extends ShortTypeNameIdResolver implements TypeIdResolver {
    private JavaType javaType = null;

    @Override
    public void init(JavaType javaType) {
        this.javaType = javaType;
    }


    @Deprecated
    public JavaType typeFromId(String typeId) {
        return TypeFactory.defaultInstance().constructType(classFromId(typeId));
    }

    @Override
    public String idFromValueAndType(Object object, Class<?> aClass) {
        return idFromValue(object);
    }

    @Override
    public String idFromBaseType() {
        return null;
    }

    @Override
    public JavaType typeFromId(DatabindContext databindContext, String typeId) {
        return TypeFactory.defaultInstance().constructType(classFromId(typeId));
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

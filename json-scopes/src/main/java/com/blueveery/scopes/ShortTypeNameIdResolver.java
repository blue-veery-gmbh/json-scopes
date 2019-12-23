package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.ProxyObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ShortTypeNameIdResolver implements TypeNameResolver {
    static private Map<String, Class> typeIdToClassMap = new ConcurrentHashMap<>();

    @Override
    public String idFromClass(Class clazz) {
        String typeId = clazz.getSimpleName();
        String[] nameComponents = typeId.split("(?<=.)(?=\\p{Lu})");
        typeId = String.join("-", nameComponents);
        typeId = typeId.toLowerCase();

        if(!typeIdToClassMap.containsKey(typeId)){

            typeIdToClassMap.put(typeId, clazz);
        }
        return typeId;
    }

    @Override
    public String idFromValue(Object object) {
        Class clazz = object.getClass();
        if(object instanceof ProxyObject){
            clazz = object.getClass().getSuperclass();
        }
        return idFromClass(clazz);
    }


    @Override
    public Class classFromId(String typeId) {
        String[] idComponents = typeId.split("/");
        if(idComponents.length<2){
            throw new IllegalStateException("Object id doesn't contains type information :" + typeId);
        }

        return typeIdToClassMap.get(idComponents[0]);
    }

    public void addPackage(Package aPackage) {

        Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner(false)).forPackages(aPackage.getName()));

        Set<Class<? extends BaseEntity>> packageClassesSet = reflections.getSubTypesOf(BaseEntity.class);
        packageClassesSet.forEach(c -> idFromClass(c));
    }
}


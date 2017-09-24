package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.ProxyObject;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ShortTypeNameIdResolver implements TypeNameResolver {
    static private Map<String, Class> typeIdToClassMap = new ConcurrentHashMap<>();

    @Override
    public String idFromClass(Class clazz) {
        String typeId = clazz.getSimpleName().toLowerCase();
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
        return typeIdToClassMap.get(idComponents[0]);
    }

    public void addPackage(Package aPackage) {

        Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner(false)).forPackages(aPackage.getName()));

        Set<Class<? extends BaseEntity>> packageClassesSet = reflections.getSubTypesOf(BaseEntity.class);
        packageClassesSet.forEach(c -> idFromClass(c));
    }
}


package com.blueveery.scopes;

public interface TypeNameResolver {
    String idFromClass(Class clazz);

    String idFromValue(Object object);

    Class classFromId(String typeId);
}

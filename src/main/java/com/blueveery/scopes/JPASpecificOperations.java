package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.MethodHandler;

import java.util.UUID;

public interface JPASpecificOperations {
    MethodHandler createTerminatingProxyHandler();
    BaseEntity unproxy(BaseEntity entity);
    boolean valueIsLoaded(Object fieldValue);
    UUID getEntityId(BaseEntity entity);
    Class[] getProxyClassInterfaces();
}

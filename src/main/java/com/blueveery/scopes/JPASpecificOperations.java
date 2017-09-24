package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.MethodHandler;

public interface JPASpecificOperations {
    MethodHandler createTerminatingProxyHandler();
    BaseEntity unproxy(BaseEntity entity);
    Class[] getProxyClassInterfaces();
}

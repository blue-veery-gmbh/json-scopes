package com.blueveery.scopes.hibernate;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.EntityReference;
import com.blueveery.scopes.JPASpecificOperations;
import javassist.util.proxy.MethodHandler;
import org.hibernate.proxy.HibernateProxy;

public class JPASpecificOperationsHibernateImpl implements JPASpecificOperations{

    private Class[] proxyClassInterfaces = {EntityReference.class, HibernateProxy.class};

    @Override
    public MethodHandler createTerminatingProxyHandler() {
        return new HibernateLazyMethodHandler();
    }

    @Override
    public BaseEntity unproxy(BaseEntity entity) {
        if (entity instanceof HibernateProxy) {
            entity = (BaseEntity) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }

    @Override
    public Class[] getProxyClassInterfaces() {
        return proxyClassInterfaces;
    }
}

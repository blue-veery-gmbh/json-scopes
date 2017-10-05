package com.blueveery.scopes.hibernate;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.EntityReference;
import com.blueveery.scopes.JPASpecificOperations;
import javassist.util.proxy.MethodHandler;
import org.hibernate.proxy.HibernateProxy;

import java.util.UUID;

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
    public boolean valueIsLoaded(Object fieldValue) {
        if(fieldValue instanceof HibernateProxy){
            HibernateProxy hibernateProxy = (HibernateProxy) fieldValue;
            return !hibernateProxy.getHibernateLazyInitializer().isUninitialized();
        }
        return true;
    }

    @Override
    public UUID getEntityId(BaseEntity entity) {
        if(entity instanceof HibernateProxy) {
            HibernateProxy hibernateProxy = (HibernateProxy) entity;
            return (UUID) hibernateProxy.getHibernateLazyInitializer().getIdentifier();
        }else {
            return entity.getId();
        }
    }

    @Override
    public Class[] getProxyClassInterfaces() {
        return proxyClassInterfaces;
    }

}

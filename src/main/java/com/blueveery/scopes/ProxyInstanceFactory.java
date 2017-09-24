package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.hibernate.proxy.HibernateProxy;

public class ProxyInstanceFactory {
    private JPASpecificOperations jpaSpecificOperations;

    public ProxyInstanceFactory(JPASpecificOperations jpaSpecificOperations) {
        this.jpaSpecificOperations = jpaSpecificOperations;
    }

    public BaseEntity createProxyInstance(Class baseClass) throws IllegalAccessException, InstantiationException {
        ProxyFactory proxyFactory = new ProxyFactory();

        proxyFactory.setSuperclass(baseClass);
        proxyFactory.setInterfaces(jpaSpecificOperations.getProxyClassInterfaces());
        Class proxyClass = proxyFactory.createClass();
        BaseEntity entity = (BaseEntity) proxyClass.newInstance();
        ((ProxyObject)entity).setHandler(jpaSpecificOperations.createTerminatingProxyHandler());
        return entity;
    }
}

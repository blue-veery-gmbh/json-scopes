package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProxyInstanceFactory {
    private JPASpecificOperations jpaSpecificOperations;

    public ProxyInstanceFactory(JPASpecificOperations jpaSpecificOperations) {
        this.jpaSpecificOperations = jpaSpecificOperations;
    }

    public ProxyObject createProxyInstance(Class baseClass) throws IllegalAccessException, InstantiationException {
        ProxyFactory proxyFactory = new ProxyFactory();
        //todo cache proxy classes
        proxyFactory.setSuperclass(baseClass);
        proxyFactory.setInterfaces(jpaSpecificOperations.getProxyClassInterfaces());
        Class proxyClass = proxyFactory.createClass();
        ProxyObject proxyObject = (ProxyObject) proxyClass.newInstance();
        proxyObject.setHandler(jpaSpecificOperations.createTerminatingProxyHandler());
        return proxyObject;
    }

    public List createListProxyInstance() {
        return jpaSpecificOperations.createListProxyInstance();
    }

    public Set createSetProxyInstance() {
        return jpaSpecificOperations.createSetProxyInstance();
    }

    public Map createMapProxyInstance() {
        return jpaSpecificOperations.createMapProxyInstance();
    }
}

package com.blueveery.scopes.hibernate;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.MethodHandler;
import org.hibernate.LazyInitializationException;
import org.hibernate.proxy.HibernateProxy;

import java.lang.reflect.Method;

/**
 * Created by tomek on 30.09.16.
 */
class HibernateLazyMethodHandler implements MethodHandler {


    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        if(thisMethod.getDeclaringClass() == BaseEntity.class || thisMethod.getDeclaringClass() == Object.class){
            return proceed.invoke(self, args);
        }
        if(thisMethod.getDeclaringClass() == HibernateProxy.class){
            if("getHibernateLazyInitializer".equals(thisMethod.getName())){
                return new ScopesLazyInitializer((BaseEntity) self);
            }
            throw new UnsupportedOperationException();
        }
        throw new LazyInitializationException(self.toString());
    }
}

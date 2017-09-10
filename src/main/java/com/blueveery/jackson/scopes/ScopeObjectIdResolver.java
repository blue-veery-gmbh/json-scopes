package com.blueveery.jackson.scopes;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.jackson.scopes.hibernate.HibernateLazyMethodHandler;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 28.09.16.
 */
public class ScopeObjectIdResolver implements ObjectIdResolver {
    private ShortNameIdResolver shortNameIdResolver = new ShortNameIdResolver();
    private Map<ObjectIdGenerator.IdKey, BaseEntity> items = new HashMap<>();
    @Override
    public void bindItem(ObjectIdGenerator.IdKey id, Object pojo) {
        if(!items.containsKey(id)) {
            items.put(id, (BaseEntity) pojo);
        }else{
            throw new IllegalStateException("id is allready bound");
        }

    }

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        try {
            BaseEntity entity = items.get(id);
            if (entity == null) {
                //todo proxy clasess need to be cached
                String idComponents[] = ((String)id.key).split("/");
                Class baseClass = shortNameIdResolver.typeFromId(null, idComponents[0]).getRawClass();
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setSuperclass(baseClass);
                proxyFactory.setInterfaces(new Class[]{EntityReference.class, HibernateProxy.class});
                Class proxyClass = proxyFactory.createClass();
                entity = (BaseEntity) proxyClass.newInstance();
                ((ProxyObject)entity).setHandler(new HibernateLazyMethodHandler());
                entity.setJsonId(id.key.toString());
                bindItem(id, entity);
            }
            return entity;
        }catch(Exception e){// ReflectiveOperationException
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new ScopeObjectIdResolver();
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return getClass() == resolverType.getClass();
    }
}

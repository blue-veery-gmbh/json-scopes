package com.blueveery.scopes.jackson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.EntityReference;
import com.blueveery.scopes.hibernate.HibernateLazyMethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 28.09.16.
 */
public class EntityResolver {
    private ShortNameIdResolver shortNameIdResolver = new ShortNameIdResolver();
    private Map<String, BaseEntity> items = new HashMap<>();
    public void bindItem(String id, BaseEntity entity) {
        if(!items.containsKey(id)) {
            items.put(id, entity);
        }else{
            throw new IllegalStateException("id is already bound");
        }

    }

    public BaseEntity resolveId(String id) {
        try {
            BaseEntity entity = items.get(id);
            if (entity == null) {
                //todo proxy clasess need to be cached
                String idComponents[] = (id).split("/");
                Class baseClass = shortNameIdResolver.typeFromId(null, idComponents[0]).getRawClass();
                ProxyFactory proxyFactory = new ProxyFactory();
                proxyFactory.setSuperclass(baseClass);
                proxyFactory.setInterfaces(new Class[]{EntityReference.class, HibernateProxy.class});
                Class proxyClass = proxyFactory.createClass();
                entity = (BaseEntity) proxyClass.newInstance();
                ((ProxyObject)entity).setHandler(new HibernateLazyMethodHandler());
                entity.setJsonId(id);
                bindItem(id, entity);
            }
            return entity;
        }catch(Exception e){
            throw new IllegalStateException(e);
        }
    }
}

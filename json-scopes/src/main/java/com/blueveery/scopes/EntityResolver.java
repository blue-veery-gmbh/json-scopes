package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import javassist.util.proxy.ProxyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 28.09.16.
 */
public class EntityResolver {
    private ProxyInstanceFactory proxyInstanceFactory;
    private TypeNameResolver typeNameResolver;
    private Map<String, BaseEntity> items = new HashMap<>();

    public EntityResolver(TypeNameResolver typeNameResolver, ProxyInstanceFactory proxyInstanceFactory) {
        this.typeNameResolver = typeNameResolver;
        this.proxyInstanceFactory = proxyInstanceFactory;
    }

    public void bindItem(String id, BaseEntity entity) {
        BaseEntity previousBaseEntity = items.get(id);
        if (previousBaseEntity == null) {
            items.put(id, entity);
        } else {
            if(previousBaseEntity instanceof ProxyObject){
                proxyInstanceFactory.initProxyWithEntity(previousBaseEntity, entity);
                items.put(id, entity);
            }else {
                throw new IllegalStateException(String.format("Id %s is already bound", id));
            }
        }

    }

    public BaseEntity resolveId(String id) {
        try {
            BaseEntity entity = items.get(id);
            if (entity == null) {
                Class baseClass = typeNameResolver.classFromId(id);
                entity = (BaseEntity) proxyInstanceFactory.createProxyInstance(baseClass);
                entity.setJsonId(id);
                bindItem(id, entity);
            }
            return entity;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

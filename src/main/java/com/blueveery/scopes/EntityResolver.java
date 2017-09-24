package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomek on 28.09.16.
 */
public class EntityResolver {
    private ProxyInstanceFactory proxyInstanceFactory;
    private ShortTypeNameIdResolver shortTypeNameIdResolver;
    private Map<String, BaseEntity> items = new HashMap<>();

    public EntityResolver(ShortTypeNameIdResolver shortTypeNameIdResolver, ProxyInstanceFactory proxyInstanceFactory) {
        this.shortTypeNameIdResolver = shortTypeNameIdResolver;
        this.proxyInstanceFactory = proxyInstanceFactory;
    }

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
                String idComponents[] = (id).split("/");
                Class baseClass = shortTypeNameIdResolver.classFromId(idComponents[0]);
                entity = proxyInstanceFactory.createProxyInstance(baseClass);
                entity.setJsonId(id);
                bindItem(id, entity);
            }
            return entity;
        }catch(Exception e){
            throw new IllegalStateException(e);
        }
    }
}

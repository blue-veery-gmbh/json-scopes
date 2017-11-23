package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.EntityResolver;
import com.blueveery.scopes.ProxyInstanceFactory;
import com.blueveery.scopes.TypeNameResolver;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseEntityDeserializer extends BaseEntityTypeAdapter implements JsonDeserializer<BaseEntity> {
    private TypeNameResolver typeNameResolver;
    private ProxyInstanceFactory proxyInstanceFactory;
    private ThreadLocal<EntityResolver> entityResolverThreadLocal = new ThreadLocal<>();

    public BaseEntityDeserializer(ReflectionUtil reflectionUtil, TypeNameResolver typeNameResolver, ProxyInstanceFactory proxyInstanceFactory) {
        super(reflectionUtil);
        this.typeNameResolver = typeNameResolver;
        this.proxyInstanceFactory = proxyInstanceFactory;
    }

    @Override
    public BaseEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

        boolean entityResolverCreated = false;
        EntityResolver entityResolver = entityResolverThreadLocal.get();
        if(entityResolver==null){
            entityResolverCreated = true;
            entityResolver = new EntityResolver(typeNameResolver, proxyInstanceFactory);
            entityResolverThreadLocal.set(entityResolver);
        }
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.size() == 1 && jsonObject.has("id")) {
                String id = jsonObject.get("id").getAsString();
                return entityResolver.resolveId(id);
            }

            String id = jsonObject.get("id").getAsString();
            Class clazz = typeNameResolver.classFromId(id);

            BaseEntity entity = (BaseEntity) clazz.newInstance();
            entity.setJsonId(id);
            entityResolver.bindItem(id, entity);

            for (Field field : reflectionUtil.getDeclaredFields(entity)) {
                if((List.class.isAssignableFrom(field.getType())) && jsonObject.get(field.getName()) == JsonNull.INSTANCE) {
                    field.set(entity, proxyInstanceFactory.createListProxyInstance());
                    continue;
                }

                if((Set.class.isAssignableFrom(field.getType())) && jsonObject.get(field.getName()) == JsonNull.INSTANCE) {
                    field.set(entity, proxyInstanceFactory.createSetProxyInstance());
                    continue;
                }

                if((Map.class.isAssignableFrom(field.getType())) && jsonObject.get(field.getName()) == JsonNull.INSTANCE) {
                    field.set(entity, proxyInstanceFactory.createMapProxyInstance());
                    continue;
                }

                if(field.getGenericType() instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    field.set(entity, context.deserialize(jsonObject.get(field.getName()), parameterizedType));
                }else {
                    field.set(entity, context.deserialize(jsonObject.get(field.getName()), field.getType()));
                }
            }


            return entity;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }finally {
            if(entityResolverCreated){
                entityResolverThreadLocal.set(null);
            }
        }
    }


}

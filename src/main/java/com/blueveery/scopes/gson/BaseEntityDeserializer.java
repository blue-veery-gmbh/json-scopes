package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.EntityResolver;
import com.blueveery.scopes.ShortTypeNameIdResolver;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class BaseEntityDeserializer extends BaseEntityTypeAdapter implements JsonDeserializer<BaseEntity> {
    private ShortTypeNameIdResolver shortTypeNameIdResolver;
    private ThreadLocal<EntityResolver> entityResolverThreadLocal = new ThreadLocal<>();

    public BaseEntityDeserializer(ReflectionUtil reflectionUtil, ShortTypeNameIdResolver shortTypeNameIdResolver) {
        super(reflectionUtil);
        this.shortTypeNameIdResolver = shortTypeNameIdResolver;
    }

    @Override
    public BaseEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

        boolean entityResolverCreated = false;
        EntityResolver entityResolver = entityResolverThreadLocal.get();
        if(entityResolver==null){
            entityResolverCreated = true;
            entityResolver = new EntityResolver(new ShortTypeNameIdResolver());
            entityResolverThreadLocal.set(entityResolver);
        }
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.size() == 1 && jsonObject.has("ref")) {
                String ref = jsonObject.get("ref").getAsString();
                return entityResolver.resolveId(ref);
            }

            String id = jsonObject.get("id").getAsString();
            Class clazz = shortTypeNameIdResolver.classFromId(id);

            BaseEntity entity = (BaseEntity) clazz.newInstance();
            entity.setJsonId(id);
            entityResolver.bindItem(id, entity);

            for (Field field : reflectionUtil.getDeclaredFields(entity)) {
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

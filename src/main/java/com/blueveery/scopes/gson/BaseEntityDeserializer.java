package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.jackson.EntityResolver;
import com.blueveery.scopes.jackson.ShortNameIdResolver;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JavaType;
import com.google.gson.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseEntityDeserializer implements JsonDeserializer<BaseEntity> {
    private ReflectionUtil reflectionUtil;
    private ShortNameIdResolver shortNameIdResolver;
    private EntityResolver entityResolver = new EntityResolver();

    public BaseEntityDeserializer(ReflectionUtil reflectionUtil, ShortNameIdResolver shortNameIdResolver) {
        this.reflectionUtil = reflectionUtil;
        this.shortNameIdResolver = shortNameIdResolver;
    }

    @Override
    public BaseEntity deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.size() == 1 && jsonObject.has("ref")) {
            String ref = jsonObject.get("ref").getAsString();
            return entityResolver.resolveId(ref);
        }

        String id = jsonObject.get("id").getAsString();
        JavaType javaType = shortNameIdResolver.typeFromId(null, id);
        try {
            BaseEntity entity = (BaseEntity) javaType.getRawClass().newInstance();
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
        }
    }


}

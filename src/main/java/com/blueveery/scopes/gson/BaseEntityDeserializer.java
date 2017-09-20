package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.jackson.EntityResolver;
import com.blueveery.scopes.jackson.ShortNameIdResolver;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JavaType;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseEntityDeserializer implements JsonDeserializer {
    private ShortNameIdResolver shortNameIdResolver;
    private EntityResolver entityResolver = new EntityResolver();

    public void setShortNameIdResolver(ShortNameIdResolver shortNameIdResolver) {
        this.shortNameIdResolver = shortNameIdResolver;
    }

    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

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

            for (Field field : getDeclaredFields(entity)) {
                field.setAccessible(true);
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

    private List<Field> getDeclaredFields(BaseEntity entity) {
        Class currentClass = entity.getClass();
        List<Field> fields = new ArrayList<>();
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && field.getAnnotation(JsonIgnore.class) == null) {
                    fields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();

        }
        return fields;
    }
}

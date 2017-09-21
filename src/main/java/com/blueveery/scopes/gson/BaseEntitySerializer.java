package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.JsonScope;
import com.blueveery.scopes.ScopeEvaluator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;


public class BaseEntitySerializer implements JsonSerializer<BaseEntity>, ScopeEvaluator {
    private JsonScope jsonScope;
    private ReflectionUtil reflectionUtil;
    private ThreadLocal<Set<BaseEntity>> serializationSetThreadLocal = new ThreadLocal<>();

    public BaseEntitySerializer(JsonScope jsonScope, ReflectionUtil reflectionUtil) {
        this.jsonScope = jsonScope;
        this.reflectionUtil = reflectionUtil;
    }

    @Override
    public JsonElement serialize(BaseEntity entity, Type type, JsonSerializationContext context) {

        boolean serializationSetCreated = false;
        Set<BaseEntity> serializationSet = serializationSetThreadLocal.get();
        if(serializationSet==null){
            serializationSetCreated = true;
            serializationSet = new HashSet<>();
            serializationSetThreadLocal.set(serializationSet);
        }

        JsonObject jsonObject = null;
        try {
            boolean isInScope = isInScope(entity, jsonScope, serializationSet);
            jsonObject = new JsonObject();

            if(!serializationSet.contains(entity) && isInScope) {
                serializationSet.add(entity);
                    jsonObject.add("id", context.serialize(entity.getJsonId()));
                    for (Field field : reflectionUtil.getDeclaredFields(entity)) {
                        Object fieldValue = field.get(entity);
                        if(field.getGenericType() instanceof ParameterizedType){
                            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                            jsonObject.add(field.getName(), context.serialize(fieldValue, parameterizedType));
                        }else {
                            jsonObject.add(field.getName(), context.serialize(fieldValue));
                        }
                    }
            }else{
                jsonObject.addProperty("ref", entity.getJsonId());
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            if(serializationSetCreated){
                serializationSetThreadLocal.set(null);
            }
        }

        return jsonObject;
    }
}

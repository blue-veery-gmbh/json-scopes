package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


public class BaseEntitySerializer extends BaseEntityTypeAdapter implements JsonSerializer<BaseEntity>, ScopeEvaluator {
    private static ThreadLocal<Set<BaseEntity>> serializationSetThreadLocal = new ThreadLocal<>();
    private TypeNameResolver typeNameResolver;
    private JPASpecificOperations jpaSpecificOperations;

    public BaseEntitySerializer(ReflectionUtil reflectionUtil, TypeNameResolver typeNameResolver, JPASpecificOperations jpaSpecificOperations) {
        super(reflectionUtil);
        this.typeNameResolver = typeNameResolver;
        this.jpaSpecificOperations = jpaSpecificOperations;
    }

    @Override
    public JsonElement serialize(BaseEntity entity, Type type, JsonSerializationContext context) {

        JsonScope jsonScope = jsonScopeThreadLocal.get();
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

            boolean serializeEntityBasedOnLazyLoad = true;
            boolean lazyLoadBordersAreEffective = jsonScope != null && jsonScope.lazyLoadBorders();
            if(lazyLoadBordersAreEffective){
                serializeEntityBasedOnLazyLoad = jpaSpecificOperations.valueIsLoaded(entity);
            }


            if(serializeEntityBasedOnLazyLoad && !serializationSet.contains(entity) && isInScope) {
                entity = jpaSpecificOperations.unproxy(entity);
                String typeName = typeNameResolver.idFromValue(entity);
                serializationSet.add(entity);
                jsonObject.add("id", context.serialize(typeName + "/" + entity.getId()));
                List<Field> declaredFields = reflectionUtil.getDeclaredFields(entity);
                for (Field field : declaredFields) {
                    Object fieldValue = field.get(entity);
                    if(Collection.class.isAssignableFrom(field.getType()) && lazyLoadBordersAreEffective && !jpaSpecificOperations.valueIsLoaded(fieldValue)){
                        jsonObject.add(field.getName(), null);
                        continue;
                    }

                    if(Map.class.isAssignableFrom(field.getType()) && lazyLoadBordersAreEffective && !jpaSpecificOperations.valueIsLoaded(fieldValue)){
                        jsonObject.add(field.getName(), null);
                        continue;
                    }

                    if(field.getGenericType() instanceof ParameterizedType){
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        jsonObject.add(field.getName(), context.serialize(fieldValue, parameterizedType));
                    }else {
                        jsonObject.add(field.getName(), context.serialize(fieldValue));
                    }
                }
            }else{
                UUID entityId = jpaSpecificOperations.getEntityId(entity);
                if(jpaSpecificOperations.valueIsLoaded(entity)){
                    entity = jpaSpecificOperations.unproxy(entity);
                }
                String typeName = typeNameResolver.idFromValue(entity);
                jsonObject.addProperty("id", typeName + "/" + entityId);
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

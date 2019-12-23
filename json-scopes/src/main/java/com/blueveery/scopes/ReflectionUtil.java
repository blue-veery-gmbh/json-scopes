package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectionUtil {

    private Set<String> ignoredFieldNames = new HashSet<>();

    public ReflectionUtil() {
        JsonIgnoreProperties jsonIgnoreProperties = BaseEntity.class.getAnnotation(JsonIgnoreProperties.class);
        if(jsonIgnoreProperties!=null){
            for(String fieldName:jsonIgnoreProperties.value()){
                ignoredFieldNames.add(fieldName);
            }
        }
    }

    public List<Field> getDeclaredFields(BaseEntity entity) {
        Class currentClass = entity.getClass();
        List<Field> fields = new ArrayList<>();
        while (currentClass != BaseEntity.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && field.getAnnotation(JsonIgnore.class) == null) {
                    if(!ignoredFieldNames.contains(field.getName())) {
                        field.setAccessible(true);
                        fields.add(field);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();

        }
        return fields;
    }
}

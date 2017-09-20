package com.blueveery.scopes.gson;

import com.blueveery.core.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {
    public List<Field> getDeclaredFields(BaseEntity entity) {
        Class currentClass = entity.getClass();
        List<Field> fields = new ArrayList<>();
        while (currentClass != Object.class) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers()) && field.getAnnotation(JsonIgnore.class) == null) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();

        }
        return fields;
    }
}

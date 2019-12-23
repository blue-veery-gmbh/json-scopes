package com.blueveery.scopes.jackson;

import com.blueveery.core.model.BaseEntity;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * Created by tomek on 23.09.16.
 */
public class ScopeSerializerModifier extends BeanSerializerModifier {
    @Override
    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
        if (BaseEntity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            return new BaseEntitySerializer((JsonSerializer<Object>) serializer);
        }
        return serializer;
    }
}

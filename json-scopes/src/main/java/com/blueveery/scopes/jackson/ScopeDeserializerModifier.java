package com.blueveery.scopes.jackson;

import com.blueveery.core.model.BaseEntity;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

/**
 * Created by tomek on 26.09.16.
 */
public class ScopeDeserializerModifier extends BeanDeserializerModifier {
    @Override
    public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
//        if (EntityReference.class.isAssignableFrom(beanDesc.getBeanClass())) {
//            return new EntityReferenceDeserializer();
//        }
        if (BaseEntity.class.isAssignableFrom(beanDesc.getBeanClass())) {
            BaseEntityDeserializer baseEntityDeserializer = new BaseEntityDeserializer(deserializer);
            return baseEntityDeserializer;
        }
        return deserializer;
    }
}


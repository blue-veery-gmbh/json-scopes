package com.blueveery.jackson.scopes;

import com.blueveery.core.model.BaseEntity;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.io.IOException;

/**
 * Created by tomek on 23.09.16.
 */
public class ScopeDeserializer extends StdDeserializer<BaseEntity> implements ScopeEvaluator, ResolvableDeserializer {

    private  JsonDeserializer<?> defaultDeserializer;

    public ScopeDeserializer(JsonDeserializer<?> deserializer) {
        super(BaseEntity.class);
        this.defaultDeserializer = deserializer;
    }

    @Override
    public BaseEntity deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        BaseEntity baseEntity = (BaseEntity) defaultDeserializer.deserialize(jsonParser, context);
        return baseEntity;
    }

    @Override
    public BaseEntity deserializeWithType(JsonParser jsonParser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        //((PublicTreeTraversingParser)jsonParser).getCurrentNode();
        JsonScope jsonScope = (JsonScope) context.getAttribute("jsonScope");
        //todo finish scope impl

        BaseEntity baseEntity = (BaseEntity) defaultDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        return baseEntity;
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        if(defaultDeserializer instanceof  ResolvableDeserializer) {
            ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
        }
    }
}

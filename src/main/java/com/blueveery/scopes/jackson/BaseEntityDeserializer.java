package com.blueveery.scopes.jackson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.EntityResolver;
import com.blueveery.scopes.JsonScope;
import com.blueveery.scopes.ScopeEvaluator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * Created by tomek on 23.09.16.
 */
public class BaseEntityDeserializer extends StdDeserializer<BaseEntity> implements ScopeEvaluator, ResolvableDeserializer {

    private  JsonDeserializer<?> defaultDeserializer;


    public BaseEntityDeserializer(JsonDeserializer<?> deserializer) {
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

        PublicTreeTraversingParser parser = (PublicTreeTraversingParser) context.getAttribute("parser");
        EntityResolver entityResolver = (EntityResolver) context.getAttribute("entityResolver");


        if(parser.getCurrentNode() instanceof ObjectNode){
            ObjectNode entityNode = (ObjectNode) parser.getCurrentNode();
            if(entityNode.size() == 1 && entityNode.has("id")) {
                return entityResolver.resolveId(parser.getCurrentNode().asText());
            }
        }

        BaseEntity baseEntity = (BaseEntity) defaultDeserializer.deserializeWithType(jsonParser, context, typeDeserializer);
        entityResolver.bindItem(baseEntity.getJsonId(), baseEntity);
        return baseEntity;
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        if(defaultDeserializer instanceof  ResolvableDeserializer) {
            ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
        }
    }
}

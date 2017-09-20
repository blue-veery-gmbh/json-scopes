package com.blueveery.scopes.jackson;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.scopes.JsonScope;
import com.blueveery.scopes.ScopeEvaluator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tomek on 23.09.16.
 */
public class BaseEntitySerializer extends StdSerializer<BaseEntity> implements ScopeEvaluator {
    private JsonSerializer<Object> defaultSerializer;
    protected BaseEntitySerializer(JsonSerializer<Object> serializer) {
        super(BaseEntity.class);
        defaultSerializer = serializer;
    }

    @Override
    public void serializeWithType(BaseEntity value, JsonGenerator jsonGenerator, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

        JsonScope jsonScope = (JsonScope) serializers.getAttribute("jsonScope");
        Set<BaseEntity> serializationSet = (Set<BaseEntity>) serializers.getAttribute(SERIALIZATION_SET);
        if(serializationSet==null){
            serializationSet = new HashSet<>();
            serializers.setAttribute(SERIALIZATION_SET,serializationSet);
        }

        boolean isInScope = isInScope(value, jsonScope, serializationSet);

        if(!serializationSet.contains(value) && isInScope) {
            serializationSet.add(value);
            defaultSerializer.serializeWithType(value, jsonGenerator, serializers, typeSer);
        }else{
            typeSer.getTypeIdResolver().idFromValue(value);//todo remove it when typeid reslover will be filed with data
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("id");
            jsonGenerator.writeObject(value.getJsonId());
            jsonGenerator.writeEndObject();
        }
    }



    @Override
    public void serialize(BaseEntity value, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException, JsonProcessingException {
        serializers.defaultSerializeValue(value, jsonGenerator);
    }
}

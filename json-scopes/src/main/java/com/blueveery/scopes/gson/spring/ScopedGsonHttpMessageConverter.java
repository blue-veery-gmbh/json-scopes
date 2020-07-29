package com.blueveery.scopes.gson.spring;

import com.blueveery.core.model.BaseEntity;
import com.google.gson.JsonIOException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.lang.Nullable;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ScopedGsonHttpMessageConverter extends GsonHttpMessageConverter {

    @Override
    protected void writeInternal(Object o, @Nullable Type type, Writer writer) throws JsonIOException {
        getGson().getAdapter(BaseEntity.class);
        if (type instanceof ParameterizedType) {
            getGson().toJson(o, type, writer);
        } else {
            getGson().toJson(o, writer);
        }
    }
}

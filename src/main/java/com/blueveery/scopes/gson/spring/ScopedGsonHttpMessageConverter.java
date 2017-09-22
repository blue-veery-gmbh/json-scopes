package com.blueveery.scopes.gson.spring;

import com.blueveery.core.model.BaseEntity;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

public class ScopedGsonHttpMessageConverter extends GsonHttpMessageConverter {

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return super.readInternal(clazz, inputMessage);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return super.read(type, contextClass, inputMessage);
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        getGson().getAdapter(BaseEntity.class);
        super.writeInternal(o, type, outputMessage);
    }
}

package com.blueveery.scopes.jackson.spring;

import com.blueveery.scopes.EntityResolver;
import com.blueveery.scopes.JsonScope;
import com.blueveery.scopes.ProxyInstanceFactory;
import com.blueveery.scopes.ShortTypeNameIdResolver;
import com.blueveery.scopes.jackson.PublicTreeTraversingParser;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.TypeUtils;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by tomek on 30.09.16.
 */
public class ScopedJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {

    private ProxyInstanceFactory proxyInstanceFactory;

    public ScopedJackson2HttpMessageConverter(ProxyInstanceFactory proxyInstanceFactory) {
        this.proxyInstanceFactory = proxyInstanceFactory;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        JavaType javaType = getJavaType(type, contextClass);
        ContextAttributes contxtAttributes = ContextAttributes.getEmpty();
        try {
            ObjectReader objectReader = objectMapper.reader();
            if(inputMessage instanceof ScopedJacksonInputMessage){
                JsonScope currentScope = ((ScopedJacksonInputMessage)inputMessage).getCurrentScope();
                contxtAttributes = contxtAttributes.withPerCallAttribute("jsonScope", currentScope);
                objectReader = objectReader.with(contxtAttributes);
            }
            if (inputMessage instanceof MappingJacksonInputMessage) {
                Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
                if (deserializationView != null) {
                    return objectReader.withView(deserializationView).forType(javaType).
                            readValue(inputMessage.getBody());
                }
            }
            JsonNode rootNode = objectMapper.readTree(inputMessage.getBody());
            PublicTreeTraversingParser jsonParser = new PublicTreeTraversingParser(rootNode);
            contxtAttributes = contxtAttributes.withPerCallAttribute("parser", jsonParser);
            objectReader = objectReader.with(contxtAttributes);
            contxtAttributes = contxtAttributes.withPerCallAttribute("entityResolver", new EntityResolver(new ShortTypeNameIdResolver(), proxyInstanceFactory));
            objectReader = objectReader.with(contxtAttributes);

            return objectReader.forType(javaType).readValue(jsonParser);
        }
        catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            writePrefix(generator, object);

            Class<?> serializationView = null;
            FilterProvider filters = null;
            Object value = object;
            JavaType javaType = null;
            JsonScope currentScope = null;
            if (object instanceof MappingJacksonValue) {
                MappingJacksonValue container = (MappingJacksonValue) object;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (object instanceof ScopedJacksonValue) {
                currentScope = ((ScopedJacksonValue)object).getCurrentScope();
            }
            if (type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
                javaType = getJavaType(type, null);
            }
            ObjectWriter objectWriter;
            if (serializationView != null) {
                objectWriter = this.objectMapper.writerWithView(serializationView);
            }
            else if (filters != null) {
                objectWriter = this.objectMapper.writer(filters);
            }else {
                objectWriter = this.objectMapper.writer();
            }
            if (javaType != null && javaType.isContainerType()) {
                objectWriter = objectWriter.forType(javaType);
            }

            if(currentScope!=null){
                ContextAttributes scopeAttr = ContextAttributes.getEmpty().withPerCallAttribute("jsonScope", currentScope);
                objectWriter = objectWriter.with(scopeAttr);
            }

            objectWriter.writeValue(generator, value);

            writeSuffix(generator, object);
            generator.flush();

        }
        catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
        }
    }

}

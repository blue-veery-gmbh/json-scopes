package com.blueveery.jackson.scopes.spring;

import com.blueveery.jackson.scopes.JsonScope;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by tomek on 23.09.16.
 */
@ControllerAdvice
public class JsonScopeRequestBodyAdvice extends RequestBodyAdviceAdapter implements JsonScopeFinder{
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return (AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType) &&
                findJsonScope(methodParameter)!=null);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
        JsonScope jsonScope = findJsonScope(methodParameter);

        return new ScopedJacksonInputMessage(inputMessage.getBody(), inputMessage.getHeaders(), jsonScope);
    }

}

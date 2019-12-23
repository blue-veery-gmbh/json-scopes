package com.blueveery.scopes.gson.spring;

import com.blueveery.scopes.JsonScope;
import com.blueveery.scopes.JsonScopeFinder;
import com.blueveery.scopes.gson.BaseEntityTypeAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by tomek on 23.09.16.
 */
@ControllerAdvice
public class JsonScopeRequestBodyAdvice extends RequestBodyAdviceAdapter implements JsonScopeFinder {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ScopedGsonHttpMessageConverter.class == converterType;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter,
                                           Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) throws IOException {
        JsonScope jsonScope = findJsonScope(methodParameter);
        BaseEntityTypeAdapter.getJsonScopeThreadLocal().set(jsonScope);

        return inputMessage;
    }

}

package com.blueveery.scopes.gson.spring;

import com.blueveery.scopes.JsonScope;
import com.blueveery.scopes.JsonScopeFinder;
import com.blueveery.scopes.gson.BaseEntityTypeAdapter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Created by tomek on 23.09.16.
 */
@ControllerAdvice
public class JsonScopeResponseBodyAdvice implements ResponseBodyAdvice<Object>, JsonScopeFinder {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return ScopedGsonHttpMessageConverter.class == converterType;
    }

    @Override
    public Object  beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response){
        JsonScope jsonScope = findJsonScope(returnType);
        BaseEntityTypeAdapter.getJsonScopeThreadLocal().set(jsonScope);

        return body;
    }

}

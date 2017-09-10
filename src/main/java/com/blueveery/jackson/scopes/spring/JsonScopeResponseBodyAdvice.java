package com.blueveery.jackson.scopes.spring;

import com.blueveery.jackson.scopes.JsonScope;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * Created by tomek on 23.09.16.
 */
@ControllerAdvice
public class JsonScopeResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice implements JsonScopeFinder {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return (AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType) &&
                ((returnType.getParameterAnnotation(JsonScope.class) != null) ||  (returnType.getDeclaringClass().getAnnotation(JsonScope.class))!=null));
    }

    protected MappingJacksonValue getOrCreateContainer(Object body) {
        return (body instanceof MappingJacksonValue ? (MappingJacksonValue) body : new ScopedJacksonValue(body));
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        ((ScopedJacksonValue)bodyContainer).setCurrentScope(findJsonScope(returnType));
    }
}

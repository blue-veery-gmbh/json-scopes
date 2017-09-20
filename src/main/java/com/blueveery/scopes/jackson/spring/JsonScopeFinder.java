package com.blueveery.scopes.jackson.spring;

import com.blueveery.scopes.JsonScope;
import org.springframework.core.MethodParameter;

/**
 * Created by tomek on 30.09.16.
 */
public interface JsonScopeFinder {
    default JsonScope findJsonScope(MethodParameter methodParameter) {
        JsonScope jsonScope = methodParameter.getMethod().getDeclaredAnnotation(JsonScope.class);
        if(jsonScope==null){
            jsonScope = methodParameter.getContainingClass().getDeclaredAnnotation(JsonScope.class);
        }
        return jsonScope;
    }
}

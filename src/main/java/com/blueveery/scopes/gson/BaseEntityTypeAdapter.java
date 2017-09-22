package com.blueveery.scopes.gson;

import com.blueveery.scopes.JsonScope;

public class BaseEntityTypeAdapter {
    protected ReflectionUtil reflectionUtil;
    protected ThreadLocal<JsonScope> jsonScopeThreadLocal = new ThreadLocal<>();

    public BaseEntityTypeAdapter(ReflectionUtil reflectionUtil) {
        this.reflectionUtil = reflectionUtil;
    }
}

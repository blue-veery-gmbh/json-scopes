package com.blueveery.scopes.gson;

import com.blueveery.scopes.JsonScope;

public class BaseEntityTypeAdapter {
    protected ReflectionUtil reflectionUtil;
    protected static ThreadLocal<JsonScope> jsonScopeThreadLocal = new ThreadLocal<>();

    public BaseEntityTypeAdapter(ReflectionUtil reflectionUtil) {
        this.reflectionUtil = reflectionUtil;
    }

    public static ThreadLocal<JsonScope> getJsonScopeThreadLocal() {
        return jsonScopeThreadLocal;
    }
}

package com.blueveery.scopes.jackson.spring;

import com.blueveery.scopes.JsonScope;
import org.springframework.http.converter.json.MappingJacksonValue;

/**
 * Created by tomek on 30.09.16.
 */
public class ScopedJacksonValue extends MappingJacksonValue {
    private JsonScope currentScope;

    public ScopedJacksonValue(Object value) {
        super(value);
    }

    public JsonScope getCurrentScope() {
        return currentScope;
    }

    public void setCurrentScope(JsonScope currentScope) {
        this.currentScope = currentScope;
    }
}

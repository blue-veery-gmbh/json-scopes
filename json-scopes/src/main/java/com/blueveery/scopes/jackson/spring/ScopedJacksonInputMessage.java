package com.blueveery.scopes.jackson.spring;

import com.blueveery.scopes.JsonScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJacksonInputMessage;

import java.io.InputStream;

/**
 * Created by tomek on 30.09.16.
 */
public class ScopedJacksonInputMessage extends MappingJacksonInputMessage {
    private JsonScope currentScope;
    public ScopedJacksonInputMessage(InputStream body, HttpHeaders headers, JsonScope currentScope) {
        super(body, headers);
        this.currentScope = currentScope;
    }

    public JsonScope getCurrentScope() {
        return currentScope;
    }
}

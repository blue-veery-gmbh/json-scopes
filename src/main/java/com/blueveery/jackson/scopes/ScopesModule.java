package com.blueveery.jackson.scopes;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Created by tomek on 23.09.16.
 */
public class ScopesModule extends SimpleModule {
    public ScopesModule() {
        super("scopes-module");

        setSerializerModifier(new ScopeSerializerModifier());
        setDeserializerModifier(new ScopeDeserializerModifier());
    }
}

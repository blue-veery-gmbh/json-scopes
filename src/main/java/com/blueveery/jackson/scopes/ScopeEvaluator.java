package com.blueveery.jackson.scopes;

import com.blueveery.core.model.BaseEntity;

import java.util.Set;

/**
 * Created by tomek on 25.09.16.
 */
public interface ScopeEvaluator {
    String SERIALIZATION_SET = "serializationSet";

    default boolean isInScope(BaseEntity value, JsonScope jsonScope, Set<BaseEntity> serializationSet) {
        boolean isInScope = false;
        if(jsonScope!=null) {
            boolean currentClassIsListed = false;
            for (Class c : jsonScope.scope()) {
                if (c.isAssignableFrom(value.getClass())) {
                    currentClassIsListed = true;
                    break;
                }
            }
            isInScope = jsonScope.positive() ? currentClassIsListed : !currentClassIsListed;
        }
        if(!isInScope && serializationSet.isEmpty()){
            isInScope = true;
        }
        return isInScope;
    }
}

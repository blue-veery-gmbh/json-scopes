package com.blueveery.scopes;

import com.blueveery.core.model.BaseEntity;

import java.util.Set;

/**
 * Created by tomek on 25.09.16.
 */
public interface ScopeEvaluator {
    String SERIALIZATION_SET = "serializationSet";

    default boolean isInScope(BaseEntity value, JsonScope jsonScope, Set<BaseEntity> serializationSet) {
        Class<? extends BaseEntity> baseEntityClass = value.getClass();
        return isInScope(baseEntityClass, jsonScope, serializationSet);
    }

    default boolean isInScope(Class<? extends BaseEntity> baseEntityClass, JsonScope jsonScope, Set<BaseEntity> serializationSet) {
        boolean isInScope = false;
        if(jsonScope!=null) {
            boolean currentClassIsListed = false;
            for (Class c : jsonScope.scope()) {
                if (c.isAssignableFrom(baseEntityClass)) {
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

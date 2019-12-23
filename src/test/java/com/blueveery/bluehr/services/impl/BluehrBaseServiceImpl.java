package com.blueveery.bluehr.services.impl;

import com.blueveery.core.services.BaseServiceImpl;
import com.blueveery.core.model.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by tomek on 09.09.16.
 */
public class BluehrBaseServiceImpl<E extends BaseEntity> extends BaseServiceImpl<E> {
    @PersistenceContext(unitName = "bluehr-pu")
    protected EntityManager entityManager;


    protected EntityManager getEntityManager() {
        return entityManager;
    }
}

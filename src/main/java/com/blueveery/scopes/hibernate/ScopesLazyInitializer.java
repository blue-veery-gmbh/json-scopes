package com.blueveery.scopes.hibernate;

import com.blueveery.core.model.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.proxy.LazyInitializer;

import java.io.Serializable;

/**
 * Created by tomek on 30.09.16.
 */
class ScopesLazyInitializer implements LazyInitializer {
    private BaseEntity baseEntity;

    public ScopesLazyInitializer(BaseEntity baseEntity) {
        this.baseEntity = baseEntity;
    }

    @Override
    public void initialize() throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Serializable getIdentifier() {
        return baseEntity.getId();
    }

    @Override
    public void setIdentifier(Serializable serializable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEntityName() {
        return baseEntity.getClass().getSuperclass().getName();
    }

    @Override
    public Class getPersistentClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUninitialized() {
        return true;
    }

    @Override
    public Object getImplementation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getImplementation(SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setImplementation(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnlySettingAvailable() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void setReadOnly(boolean b) {

    }

    @Override
    public SharedSessionContractImplementor getSession() {
        return null;
    }

    @Override
    public void setSession(SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unsetSession() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setUnwrap(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUnwrap() {
        throw new UnsupportedOperationException();
    }
}

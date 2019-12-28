package com.blueveery.core.services;

import com.blueveery.core.model.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;

/**
 * Created by tomek on 09.09.16.
 */
public abstract class BaseServiceImpl<E extends BaseEntity> implements BaseService<E> {

    private Class entityType;

    public BaseServiceImpl() {
        entityType = ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected abstract EntityManager getEntityManager();

    @Transactional
    @Override
    public E find(UUID id) {
        E entity = (E) getEntityManager().find(entityType, id);
        if (entity == null) {
            throw new NoResultException();
        }
        return doFind(entity);
    }

    protected E doFind(E entity) {
        return entity;
    }

    @Transactional
    @Override
    public List<E> findAll() {
        TypedQuery<E> typedQuery = getFindAllTypedQuery();
        return typedQuery.getResultList();
    }

    @Transactional
    @Override
    public List<E> paginatedFindAll(Integer pageNumber, Integer pageSize) {
        TypedQuery<E> typedQuery = getFindAllTypedQuery();
        if (pageNumber != null && pageSize != null) {
            addPagination(typedQuery, pageNumber, pageSize);
        }
        return typedQuery.getResultList();
    }

    private TypedQuery<E> getFindAllTypedQuery() {
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(entityType);
        Root<E> rootEntity = criteriaQuery.from(entityType);
        CriteriaQuery<E> selectAllQuery = criteriaQuery.select(rootEntity);
        return getEntityManager().createQuery(selectAllQuery);
    }


    @Transactional
    @Override
    public E merge(E entity) {
        validateBeforeUpdate(entity);
        updateObjectBeforeMerge(entity, (E) getEntityManager().find(entityType, entity.getId()));
        return doMerge(entity);
    }

    protected E doMerge(E entity) {
        return getEntityManager().merge(entity);
    }

    protected void updateObjectBeforeMerge(E modifiedEntity, E databaseEntity) {

    }

    protected void validateBeforeUpdate(E entity) {
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        E entity = find(id);
        validateBeforeDelete(entity);
        getEntityManager().remove(entity);
    }

    protected void validateBeforeDelete(E entity) {
    }

    protected <E extends BaseEntity> void addPagination(TypedQuery<E> typedQuery, int pageNumber, int pageSize) {
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);
    }
}

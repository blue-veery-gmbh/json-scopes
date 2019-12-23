package com.blueveery.core.services;

import com.blueveery.core.model.BaseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Created by tomek on 09.09.16.
 */
public interface BaseService<E extends BaseEntity> {
    E find(UUID id);

    E merge(E entity);

    List<E> findAll();

    List<E> paginatedFindAll(Integer pageNumber, Integer pageSize);

    void delete(UUID id);
}

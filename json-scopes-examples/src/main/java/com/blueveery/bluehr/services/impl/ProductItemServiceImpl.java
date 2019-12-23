package com.blueveery.bluehr.services.impl;

import com.blueveery.bluehr.model.*;
import com.blueveery.bluehr.services.api.ProductItemService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class ProductItemServiceImpl extends OrdersBaseServiceImpl<ProductItem> implements ProductItemService {

    @Transactional
    @Override
    public ProductItem findProductItem(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductItem> criteriaQuery = criteriaBuilder.createQuery(ProductItem.class);
        Root<ProductItem> enrollmentRoot = criteriaQuery.from(ProductItem.class);
        Predicate emaiPredicate = criteriaBuilder.equal(enrollmentRoot.get("name"), name);
        CriteriaQuery<ProductItem> selectAllQuery = criteriaQuery.select(enrollmentRoot).where(emaiPredicate);

        return entityManager.createQuery(selectAllQuery).getResultList().stream().findFirst().orElse(null);
    }

}

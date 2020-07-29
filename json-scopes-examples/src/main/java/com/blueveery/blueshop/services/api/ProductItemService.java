package com.blueveery.blueshop.services.api;

import com.blueveery.blueshop.model.ProductItem;
import com.blueveery.core.services.BaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tomek on 09.09.16.
 */
public interface ProductItemService extends BaseService<ProductItem> {
    ProductItem findProductItem(String name);
}

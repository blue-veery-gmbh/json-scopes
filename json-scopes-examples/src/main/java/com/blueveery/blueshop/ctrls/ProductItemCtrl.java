package com.blueveery.blueshop.ctrls;

import com.blueveery.blueshop.model.ProductItem;
import com.blueveery.blueshop.services.api.ProductItemService;
import com.blueveery.core.ctrls.CreateObjectCtrl;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * Created by tomek on 07.09.16.
 */
@Component
@Secured("ROLE_ADMIN")
@RequestMapping("/api/product-item")
public class ProductItemCtrl implements GetObjectCtrl<ProductItem>,
                                        FindAllCtrl<ProductItem>,
                                        CreateObjectCtrl<ProductItem>,
                                        UpdateObjectCtrl<ProductItem> {

    @Autowired
    private ProductItemService productItemService;

    @Override
    public BaseService<ProductItem> getService() {return productItemService;}


    @Secured({"ROLE_TEST", "ROLE_ADMIN"})
    @Override
    public ProductItem doGetObject(UUID id) {
        return productItemService.find(id);
    }

    @Override
    public ProductItem doUpdateObject(ProductItem productItem) {
        return productItemService.merge(productItem);
    }

}

package com.blueveery.blueshop.ctrls;

import com.blueveery.blueshop.model.Customer;
import com.blueveery.blueshop.model.Order;
import com.blueveery.blueshop.model.Person;
import com.blueveery.blueshop.model.ProductItem;
import com.blueveery.blueshop.services.api.OrderService;
import com.blueveery.core.ctrls.CreateObjectCtrl;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by tomek on 08.09.16.
 */
@Component
@JsonScope(positive = true,
           scope = {Order.class, Customer.class, Person.class, ProductItem.class}
)
@RequestMapping("/api/order")
public class OrderCtrl implements GetObjectCtrl<Order>,
                                  CreateObjectCtrl<Order>,
                                  UpdateObjectCtrl<Order>,
                                  FindAllCtrl<Order> {

    @Autowired
    private OrderService orderService;

    @Override
    public BaseService<Order> getService() {
        return orderService;
    }

    @JsonScope(positive = true, scope = {Order.class})
    @Override
    public List<Order> findAll() {
        return getService().findAll();
    }
}

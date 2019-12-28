package com.blueveery.blueshop.services.impl;

import com.blueveery.blueshop.model.Order;
import com.blueveery.blueshop.services.api.OrderService;
import org.springframework.stereotype.Component;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class OrderServiceImpl extends OrdersBaseServiceImpl<Order> implements OrderService {
    @Override
    protected Order doFind(Order order) {
        order.getCustomer().getPerson().getId(); // load customer role nad person
        order.getProductItemList().size();       // load product items
        return order;
    }
}

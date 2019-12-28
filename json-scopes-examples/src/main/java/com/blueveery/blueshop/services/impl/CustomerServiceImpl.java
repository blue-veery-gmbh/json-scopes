package com.blueveery.blueshop.services.impl;

import com.blueveery.blueshop.model.Customer;
import com.blueveery.blueshop.services.api.CustomerService;
import org.springframework.stereotype.Component;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class CustomerServiceImpl extends OrdersBaseServiceImpl<Customer> implements CustomerService {

    @Override
    protected Customer doFind(Customer customer) {
        customer.getPerson().getVersion(); // touch connections
        customer.getOrderList().size();    // touch connections
        return customer;
    }
}

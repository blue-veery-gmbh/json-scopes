package com.blueveery.blueshop.services.impl;

import com.blueveery.blueshop.model.Customer;
import com.blueveery.blueshop.services.api.CustomerService;
import org.springframework.stereotype.Component;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class CustomerServiceImpl extends OrdersBaseServiceImpl<Customer> implements CustomerService {
}

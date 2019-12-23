package com.blueveery.bluehr.services.impl;

import com.blueveery.bluehr.model.Customer;
import com.blueveery.bluehr.services.api.CustomerService;
import org.springframework.stereotype.Component;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class CustomerServiceImpl extends OrdersBaseServiceImpl<Customer> implements CustomerService {
}

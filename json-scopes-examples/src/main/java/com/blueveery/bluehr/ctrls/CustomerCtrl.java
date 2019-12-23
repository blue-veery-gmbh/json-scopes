package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.Customer;
import com.blueveery.bluehr.model.Order;
import com.blueveery.bluehr.services.api.CustomerService;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

/**
 * Created by tomek on 08.09.16.
 */
@Component
@JsonScope(positive = true, scope = {Customer.class})
@RequestMapping("/api/customer")
public class CustomerCtrl implements GetObjectCtrl<Customer>,
                                         UpdateObjectCtrl<Customer>,
                                         FindAllCtrl<Customer> {

    @Autowired
    private CustomerService customerService;

    @Override
    public BaseService<Customer> getService() {
        return customerService;
    }


    @JsonScope(positive = true, lazyLoadBorders = true, scope = {Order.class})
    @Override
    public Customer getObject(@PathVariable("id") UUID id) {
        return getService().find(id);
    }
}

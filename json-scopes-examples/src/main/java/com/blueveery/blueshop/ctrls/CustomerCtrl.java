package com.blueveery.blueshop.ctrls;

import com.blueveery.blueshop.model.Customer;
import com.blueveery.blueshop.services.api.CustomerService;
import com.blueveery.core.ctrls.CreateObjectCtrl;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by tomek on 08.09.16.
 */
@Component
@JsonScope(positive = true, scope = {Customer.class})
@RequestMapping("/api/customer")
public class CustomerCtrl implements GetObjectCtrl<Customer>,
                                     CreateObjectCtrl<Customer>,
                                     UpdateObjectCtrl<Customer>,
                                     FindAllCtrl<Customer> {

    @Autowired
    private CustomerService customerService;

    @Override
    public BaseService<Customer> getService() {
        return customerService;
    }
}

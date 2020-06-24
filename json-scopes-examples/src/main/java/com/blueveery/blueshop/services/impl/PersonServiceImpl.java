package com.blueveery.blueshop.services.impl;

import com.blueveery.blueshop.model.Person;
import com.blueveery.blueshop.services.api.PersonService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class PersonServiceImpl extends OrdersBaseServiceImpl<Person> implements PersonService {

    public List<Person> findAllWithLocation() {
        List<Person> all = super.findAll();
        all.forEach(p -> p.getLocation().getStreetName()); // touch object
        return all;
    }
}

package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.Person;
import com.blueveery.bluehr.services.api.PersonService;
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
@JsonScope(positive = true, scope = {Person.class})
@RequestMapping("/api/person")
public class PersonCtrl implements CreateObjectCtrl<Person>,
                                         FindAllCtrl<Person> {

    @Autowired
    private PersonService personService;

    @Override
    public BaseService<Person> getService() {return personService;}
}

package com.blueveery.bluehr.services.impl;

import com.blueveery.bluehr.model.Consultant;
import com.blueveery.bluehr.model.EnrollmentStatus;
import com.blueveery.bluehr.model.Person;
import com.blueveery.bluehr.services.ConsultantService;
import com.blueveery.bluehr.services.OperationNotAllowedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class ConsultantServiceImpl extends BluehrBaseServiceImpl<Consultant> implements ConsultantService {

    @Transactional
    @Override
    public Consultant find(UUID id) {
        Consultant consultant = super.find(id);
//        consultant.getCvDocument().getId();
        consultant.getEnrollment().getId();
        consultant.getPerson().getVersion(); //.getPersonRoles().size();

        return consultant;
    }

    @Transactional
    @Override
    public Consultant merge(Consultant updatedConsultant) {
        entityManager.merge(updatedConsultant);
        Person person = entityManager.merge(updatedConsultant.getPerson());
        System.out.println(person.getPersonRoles().size());
        return null;
    }
}

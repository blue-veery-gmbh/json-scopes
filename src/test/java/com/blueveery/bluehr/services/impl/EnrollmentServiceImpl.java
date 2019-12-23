package com.blueveery.bluehr.services.impl;

import com.blueveery.bluehr.model.*;
import com.blueveery.bluehr.services.EnrollmentExistException;
import com.blueveery.bluehr.services.EnrollmentService;
import com.blueveery.bluehr.services.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by tomek on 09.09.16.
 */
@Component
public class EnrollmentServiceImpl extends BluehrBaseServiceImpl<Enrollment> implements EnrollmentService {

    @Autowired
    private SystemConfigService systemConfigService;

    @Transactional
    @Override
    public UUID enroll(String email) {
        email = email.toLowerCase();
        if(findEnrollment(email) != null){
            throw new EnrollmentExistException(email);
        }


        Enrollment enrollment = new Enrollment();
        enrollment.setEmail(email);

        Person person = new Person();
        Consultant consultant = new Consultant();
        consultant.setEnrollment(enrollment);
        consultant.setPerson(person);
        consultant.setEmail(email);
        person.getPersonRoles().add(consultant);
        consultant.setCvDocument(new CVDocument());
        enrollment.setConsultant(consultant);
        //sendEmail(enrollment);

        entityManager.persist(enrollment);
        entityManager.persist(consultant);
        return enrollment.getId();
    }




    private Enrollment findEnrollment(String email) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Enrollment> criteriaQuery = criteriaBuilder.createQuery(Enrollment.class);
        Root<Enrollment> enrollmentRoot = criteriaQuery.from(Enrollment.class);
        Predicate emaiPredicate = criteriaBuilder.equal(enrollmentRoot.get("email"), email);
        CriteriaQuery<Enrollment> selectAllQuery = criteriaQuery.select(enrollmentRoot).where(emaiPredicate);

        return entityManager.createQuery(selectAllQuery).getResultList().stream().findFirst().orElse(null);
    }

    @Transactional
    @Override
    public List<Enrollment> findEnrollments(String status, int page, int pageSize) {
        return null;//TODO
    }




}

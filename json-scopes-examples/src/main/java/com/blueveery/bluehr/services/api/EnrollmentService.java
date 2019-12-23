package com.blueveery.bluehr.services;

import com.blueveery.bluehr.model.Enrollment;
import com.blueveery.core.services.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * Created by tomek on 09.09.16.
 */
public interface EnrollmentService extends BaseService<Enrollment> {
    UUID enroll(String email);

    List<Enrollment> findEnrollments(String status, int page, int pageSize);
}

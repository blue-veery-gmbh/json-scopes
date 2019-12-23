package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.Enrollment;
import com.blueveery.bluehr.services.EnrollmentService;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/**
 * Created by tomek on 07.09.16.
 */
@Component
@Secured("ROLE_ADMIN")
@RequestMapping("/api/enrollment")
public class EnrollmentCtrl implements GetObjectCtrl<Enrollment>,
                                        FindAllCtrl<Enrollment>,
                                        UpdateObjectCtrl<Enrollment> {

    @Autowired
    private EnrollmentService enrollmentService;

    @Override
    public BaseService<Enrollment> getService() {
        return enrollmentService;
    }


    @Secured({"ROLE_TEST", "ROLE_ADMIN"})
    @Override
    public Enrollment doGetObject(UUID id) {
        return enrollmentService.find(id);
    }

    @Override
    public Enrollment doUpdateObject(Enrollment enrollment) {
        enrollmentService.merge(enrollment);
        return null;
    }

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json"})
    @ResponseBody
    public UUID enroll(@RequestParam("email") String email) {
        return enrollmentService.enroll(email);
    }


    @RequestMapping(path = "/whoami/{text-two}", method = RequestMethod.GET)
    @ResponseBody
    public String whoami(@RequestParam(required = false, defaultValue = "free text", name = "paramName") String textOne, @PathVariable(value = "text-two") String textTwo, int i) {
        String userName = "";
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof Principal){
            userName = ((Principal)user).getName();
        }else{
            userName = user.toString();
        }
        return "Current user is " + userName;
    }
}

package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.Consultant;
import com.blueveery.bluehr.model.Enrollment;
import com.blueveery.bluehr.services.ConsultantService;
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
@JsonScope(positive = false, scope = {Enrollment.class})
@RequestMapping("/api/consultant")
public class ConsultantCtrl implements GetObjectCtrl<Consultant>,
        UpdateObjectCtrl<Consultant>,
        FindAllCtrl<Consultant> {

    @Autowired
    private ConsultantService consultantService;

    @Override
    public BaseService<Consultant> getService() {
        return consultantService;
    }


    @JsonScope(positive = false, lazyLoadBorders = true, scope = {})
    @Override
    public Consultant getObject(@PathVariable("id") UUID id) {
        return getService().find(id);
    }
}

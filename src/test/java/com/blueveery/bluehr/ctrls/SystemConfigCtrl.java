package com.blueveery.bluehr.ctrls;

import com.blueveery.bluehr.model.SystemConfig;
import com.blueveery.bluehr.services.SystemConfigService;
import com.blueveery.core.ctrls.FindAllCtrl;
import com.blueveery.core.ctrls.UpdateObjectCtrl;
import com.blueveery.core.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

/**
 * Created by tomek on 09.09.16.
 */
@Component
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/api/system-config")
public class SystemConfigCtrl implements UpdateObjectCtrl<SystemConfig>, FindAllCtrl<SystemConfig>{

    @Autowired
    private SystemConfigService systemConfigService;

    @Override
    public BaseService<SystemConfig> getService() {
        return systemConfigService;
    }
}

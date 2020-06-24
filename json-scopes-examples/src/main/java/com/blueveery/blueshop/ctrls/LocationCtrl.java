package com.blueveery.blueshop.ctrls;

import com.blueveery.blueshop.model.Location;
import com.blueveery.blueshop.services.api.LocationService;
import com.blueveery.core.ctrls.GetObjectCtrl;
import com.blueveery.core.services.BaseService;
import com.blueveery.scopes.JsonScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@JsonScope(positive = true, scope = {Location.class})
@RequestMapping("/api/location")
public class LocationCtrl implements GetObjectCtrl<Location> {

    @Autowired
    LocationService locationService;

    @Override
    public BaseService<Location> getService() {
        return this.locationService;
    }

}

package com.blueveery.bluehr.services;

import com.blueveery.bluehr.model.SystemConfig;
import com.blueveery.core.services.BaseService;

/**
 * Created by tomek on 09.09.16.
 */
public interface SystemConfigService extends BaseService<SystemConfig> {
    SystemConfig getSystemConfig(String name);
}
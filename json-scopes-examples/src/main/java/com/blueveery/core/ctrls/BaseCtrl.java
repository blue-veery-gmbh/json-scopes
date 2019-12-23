package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import com.blueveery.core.services.BaseService;

/**
 * Created by tomek on 21.09.16.
 */
public interface BaseCtrl<E extends BaseEntity> {

    BaseService<E> getService();
}

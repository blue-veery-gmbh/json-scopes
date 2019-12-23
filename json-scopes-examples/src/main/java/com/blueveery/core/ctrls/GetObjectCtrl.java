package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * Created by tomek on 15.09.16.
 */
public interface GetObjectCtrl<E extends BaseEntity> extends BaseCtrl<E> {

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    default E getObject(@PathVariable("id") UUID id) {
        return doGetObject(id);
    }

    default E doGetObject(UUID id) {
        return getService().find(id);
    }

}

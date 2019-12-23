package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface CreateObjectCtrl<E extends BaseEntity> extends BaseCtrl<E> {

    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    default E createObject(@RequestBody BaseEntity entity) {
        return doCreateObject((E) entity);
    }

    default E doCreateObject(E entity) {
        return getService().merge(entity);
    }
}

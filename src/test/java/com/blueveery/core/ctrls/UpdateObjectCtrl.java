package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by tomek on 19.09.16.
 */
public interface UpdateObjectCtrl<T extends BaseEntity> extends BaseCtrl<T> {

    @CrossOrigin
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
    @ResponseBody
    default T updateObject(@RequestBody BaseEntity entity) {
        return doUpdateObject((T) entity);
    }

    default T doUpdateObject(T entity) {
        return getService().merge(entity);
    }
}

package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface DeleteObjectCtrl<E extends BaseEntity> extends BaseCtrl<E> {

    @CrossOrigin
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    default void deleteObject(@PathVariable("id") UUID id) {
        doDeleteObject(id);
    }

    default void doDeleteObject(UUID id) {
        getService().delete(id);
    }

}

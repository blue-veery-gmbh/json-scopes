package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by tomek on 19.09.16.
 */
public interface FindAllCtrl<E extends BaseEntity> extends BaseCtrl<E> {

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"})
    @ResponseBody
    default List<E> findAll() {
        return doFindAll();
    }

    default List<E> doFindAll() {
        List<E> list = getService().findAll();
        return list;
    }
}

package com.blueveery.core.ctrls;

import com.blueveery.core.model.BaseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface PaginatedFindAll<E extends BaseEntity> extends BaseCtrl<E> {

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json"}, params = {"pageNumber", "pageSize"})
    @ResponseBody
    default List<E> paginatedFindAll(@RequestParam(value = "pageNumber") Integer pageNumber,
                                     @RequestParam(value = "pageSize") Integer pageSize) {
        return doPaginatedFindAll(pageNumber, pageSize);
    }

    default List<E> doPaginatedFindAll(Integer pageNumber, Integer pageSize) {
        return getService().paginatedFindAll(pageNumber, pageSize);
    }
}

package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.LockType;
import com.urent.server.service.LockTypeService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * LockTypeController
 * <p/>
 * Created by Administrator on 2015/8/15
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class LockTypeController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LockTypeService lockTypeService;

    /**
     * 查询LockType对象列表
     *
     * @param start
     * @param limit
     * @param filterText
     * @param sortText
     * @return {
     * total:  #{total}
     * list: [ {
     * }
     * ]
     * }
     */
    @JsonView({View.Summary.class})
    @RequestMapping(value = "/lockType", method = RequestMethod.GET)
    public Map<String, Object> getLockTypes(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false) String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<LockType> list = lockTypeService.getLockTypes(queryFilter);
        Long total = lockTypeService.getLockTypeCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询LockType对象
     *
     * @param id
     * @return LockType对象
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/lockType/{id}", method = RequestMethod.GET)
    public LockType getLockTypeById(@PathVariable(value = "id") long id) {
        return lockTypeService.getLockTypeById(id);
    }

    /**
     * 增加LockType对象
     *
     * @param lockType
     * @return LockType对象
     */
    @RequestMapping(value = "/lockType", method = RequestMethod.POST)
    public LockType addLockType(@RequestBody @Valid LockType lockType) {
        return lockTypeService.addLockType(lockType);
    }


    /**
     * 更新LockType对象
     *
     * @param lockType
     * @param id
     * @return LockType对象
     */
    @RequestMapping(value = "/lockType/{id}", method = RequestMethod.PUT)
    public LockType updateLockType(@RequestBody LockType lockType, @PathVariable("id") long id) {
        lockTypeService.updateLockType(lockType);
        return lockType;
    }

    /**
     * 删除LockType对象，注意一个LockType被删除的前提是没有被Lock引用过，有被引用的LockType原则上是不允许被删除的
     *
     * @param lockType
     * @return {
     * success: true
     * }
     */
    @RequestMapping(value = "/lockType/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteLockType(LockType lockType) {
        lockTypeService.deleteLockType(lockType);
        return SuccessNoDataResult.getSuccessResult();
    }

    /**
     * 定位LockType对象列表
     *
     * @param start
     * @param limit
     * @param filterText
     * @param sortText
     * @return {
     * total:  #{total}
     * list: [ {
     * }
     * ]
     * }
     */
    @JsonView({View.Summary.class})
    @RequestMapping(value = "/locateLockType", method = RequestMethod.GET)
    public Map<String, Object> locateLockTypes(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false) String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<LockType> list = lockTypeService.getLockTypes(queryFilter);
        Long total = lockTypeService.getLockTypeCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }
}

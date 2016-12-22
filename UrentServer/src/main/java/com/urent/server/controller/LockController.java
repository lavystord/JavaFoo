package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.House;
import com.urent.server.domain.Lock;
import com.urent.server.service.HouseService;
import com.urent.server.service.LockService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LockController
 * <p/>
 * Created by Administrator on 2015/8/16
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class LockController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    LockService lockService;

    @Autowired
    HouseService houseService;
    /**
     * 查询Lock对象列表
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
    @RequestMapping(value = "/lock", method = RequestMethod.GET)
    public Map<String, Object> getLocks(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                        @RequestParam(value = "filter", required = false) String filterText,
                                        @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<Lock> list = lockService.getLocks(queryFilter);
        Long total = lockService.getLockCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询Lock对象
     *
     * @param id
     * @return Lock对象
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/lock/{id}", method = RequestMethod.GET)
    public Lock getLockById(@PathVariable(value = "id") long id) {
        return lockService.getLockById(id);
    }

    /**
     * 增加Lock对象
     *
     * @param lock
     * @return Lock对象
     */
    @RequestMapping(value = "/lock", method = RequestMethod.POST)
    public Lock addLock(@RequestBody @Valid Lock lock) {
        return lockService.addLock(lock);
    }


    /**
     * 更新Lock对象
     *
     * @param lock
     * @param id
     * @return Lock对象
     * @attention 禁用此接口
     */
   /* @RequestMapping(value = "/lock/{id}", method = RequestMethod.PUT)
    public Lock updateLock(@RequestBody Lock lock, @PathVariable("id") long id) {
        lockService.updateLock(lock);
        return lock;
    }*/

    /**
     * 删除Lock对象
     *
     * @param lock
     * @return {
     * success: true
     * }
     */
    @RequestMapping(value = "/lock/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteLock(Lock lock) {
        lockService.deleteLock(lock);
        return SuccessNoDataResult.getSuccessResult();
    }

    @RequestMapping(value = "/lock/deactivate", method = RequestMethod.POST)
    public Map<String, Boolean> deactivateLock(@RequestBody Lock lock) {
        lockService.deactivateLock(lock);
        return SuccessNoDataResult.getSuccessResult();
    }


    @JsonView({View.Summary.class})
    @RequestMapping(value = "/locateLock", method = RequestMethod.GET)
    public Map<String, Object> locateLock(@RequestParam(value = "gapAddress", required = true) String gapAddress) {
        List<Lock> list = lockService.locateLock(gapAddress);
        return CommonDataFormatTool.formatListResult((long) list.size(), list);
    }


    @RequestMapping(value = "/registerLock", method = RequestMethod.POST)
    public Lock registerLock(@RequestBody Lock lock) {
        return lockService.addLock(lock);
    }


    @RequestMapping(value = "/setOwner", method = RequestMethod.POST)
    public Map<String, Boolean> setOwner(@RequestBody Lock lock) {
        // 首先检查这个lock的gap地址是否与房屋关系对应
        Lock lock1 = lockService.getLockById(lock.getId());
        if(!lock.getHouse().getId().equals(lock1.getHouse().getId()) || !lock.getGapAddress().equals(lock1.getGapAddress())) {
            throw new USException(USException.ErrorCode.IllegalRequestParam, "锁的GAP地址与房屋信息不对应");
        }
        // 再检查房屋上是否已存在主人
        House house = houseService.getHouseById(lock.getHouse().getId()) ;
        if(house.getOwner() != null) {
            throw new USException(USException.ErrorCode.OwnerExistsOnHouse);
        }

        // 更新房主，同时系统会为房主分配这把锁上的主钥匙
        // 这里为了防止前台传入了多余的数据导致更新失败（如果传入了地址，则会导致更新同一门牌的房屋）
        House house1 = new House();
        house1.setId(lock.getHouse().getId());
        house1.setOwner(lock.getHouse().getOwner());
        houseService.updateHouse(house1);
        return SuccessNoDataResult.getSuccessResult();
    }
}

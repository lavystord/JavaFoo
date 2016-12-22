package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.Address;
import com.urent.server.domain.Version;
import com.urent.server.domain.View;
import com.urent.server.service.AddressService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/13.
 */
@RestController
public class AddressController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AddressService addressService;

    /**
     * 生成一条新的address
     * @param address
     * @return
     *
     */
    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public Address addAddress(@RequestBody Address address){

        return addressService.addAddress(address);
    }

    /**
     * 查询address列表
     * @param start
     * @param limit
     * @param filterText：过滤条件，过滤的条件暂时只支持(subDistrict like {}"%")
     * @param sortText
     * @return
     */
    @JsonView({View.Summary.class})
    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public Map<String, Object> getAddresses(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                           @RequestParam(value = "filter", required = false)String filterText,
                                           @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<Address> list=addressService.getAddresses(queryFilter);
        Long total=addressService.getAddressCount(queryFilter);
        return CommonDataFormatTool.formatListResult(total, list);

    }

    @JsonView({View.Detail.class})
    @RequestMapping(value = "/address/{id}", method = RequestMethod.GET)
    public Address getAddress(@PathVariable("id") Long id) {
        return addressService.getAddress(id);
    }

    /**
     * 更新一个Address信息，更新的域可能是 (area.id, subDistrict, longitude, latitude)
     * @param address
     * @param id
     * @return
     */
    @RequestMapping(value = "/address/{id}", method = RequestMethod.PUT)
    public Address updateAddress(@RequestBody Address address, @PathVariable("id") Long id) {

        return addressService.updateAddress(address);
    }





    /**
     * 删除一个Address信息
     * @param address
     * @param id
     * @return
     * 删除时先检查一下这个Address是否被有效的HOUSE引用，原则上一个address一旦被引用，则不能再删除
     */
    @RequestMapping(value = "/address/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteAddress(@RequestBody Address address, @PathVariable("id") Long id) {
        addressService.deleteAddress(address);
        return  SuccessNoDataResult.getSuccessResult();
    }



    /**
     * 定位address列表
     * @param start
     * @param limit
     * @param filterText：过滤条件，过滤的条件会传入longitude和latitude，期望做一个KNN查询，但前期不支持，暂时只支持(subDistrict like {}"%")
     * @param sortText
     * @return
     */
    @JsonView({View.Summary.class})
    @RequestMapping(value = "/locateAddress", method = RequestMethod.GET)
    public Map<String, Object> locateAddresses(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<Address> list=addressService.locateAddresses(queryFilter);
        Long total=addressService.locateAddressCount(queryFilter);
        return CommonDataFormatTool.formatListResult(total, list);

    }

}

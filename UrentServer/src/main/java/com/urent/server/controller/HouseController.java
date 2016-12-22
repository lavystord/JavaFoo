package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.Address;
import com.urent.server.domain.House;
import com.urent.server.domain.View;
import com.urent.server.service.HouseService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/14.
 */
@RestController
public class HouseController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    HouseService houseService;

    /**
     * 查询house列表
     * @param start
     * @param limit
     * @param filterText：过滤条件，过滤的条件暂时支持{
     *                  subdistrict like {}"%",
     *                  name like {}"%",
     *                  mobile like {}"%"
     *                  }
     * @param sortText  排序条件， 暂时不用支持，对房屋的排序没啥意义
     * @return
     */
    @JsonView(View.Summary.class)
    @RequestMapping(value = "/house", method = RequestMethod.GET)
    public Map<String, Object> getHouses(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, null,
                objectMapper);
        List<House> list = houseService.getHouses(queryFilter);
        Long total = houseService.getHouseCount(queryFilter);

        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 生成一条新的house
     * @param house
     * @return
     *
     */
    @RequestMapping(value = "/house", method = RequestMethod.POST)
    public House addHouse(@RequestBody House house){
        return houseService.addHouse(house);
    }


    /**
     * 得到house的详细信息
     * @param id
     * @return
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/house/{id}", method = RequestMethod.GET)
    public House getHouse(@PathVariable("id") Long id) {
        return houseService.getHouseById(id);
    }


    /**
     * 更新一个House信息，更新的域可能是 (address.id, owner.id, unit, floor, number, building)
     * @param house
     * @param id
     * @return
     */
    @RequestMapping(value = "/house/{id}", method = RequestMethod.PUT)
    public House updateHouse(@RequestBody House house, @PathVariable("id") Long id) {
        return houseService.updateHouse(house);
    }

    /**
     * 更新一个House信息，更新的域可能是 (address.id, owner.id, unit, floor, number, building)
     * @param house
     * @param id
     * @return
     */
    @RequestMapping(value = "/house/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteHouse(@RequestBody House house, @PathVariable("id") Long id) {
        houseService.deleteHouse(house);
        return SuccessNoDataResult.getSuccessResult();
    }

    @RequestMapping(value = "/locateHouse", method = RequestMethod.GET)
    public Map<String, Object> locateHouses(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, null,
                objectMapper);
        List<House> list = houseService.getHouses(queryFilter);
        Long total = houseService.getHouseCount(queryFilter);

        return CommonDataFormatTool.formatListResult(total, list);
    }


    @RequestMapping(value = "/registerHouse", method = RequestMethod.POST)
    public House registerHouse(@RequestBody House house) {
        return houseService.addHouse(house);
    }
}

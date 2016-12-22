package com.urent.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.Area;
import com.urent.server.service.AreaService;
import com.urent.server.util.CommonDataFormatTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/13.
 */
@RestController
public class AreaController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AreaService areaService;

    /**
     * 查询Area信息
     * @param start
     * @param limit
     * @param filterText  这里最可能的filter条件是(parentId =)
     * @param sortText
     * @return
     */

    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public Map<String, Object> getAreas(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                               @RequestParam(value = "filter", required = false)String filterText,
                               @RequestParam(value = "sort", required = false) String sortText) {

        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<Area> list = areaService.getAreas(queryFilter);
        Long total = areaService.getAreaCount(queryFilter);
        return CommonDataFormatTool.formatListResult(total, list);
    }

}

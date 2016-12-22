package com.urent.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.service.MethodService;
import com.urent.server.util.CommonDataFormatTool;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * MethodController
 * <p/>
 * Created by Administrator on 2015/8/20
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class MethodController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MethodService methodService;


    /**
     * 查询不关联的方法
     *
     * @param filterText : 过滤条件，一定是roleId = ? and urlId = ?
     * @return Method对象
     */
    @RequestMapping(value = "/unrelatedMethods", method = RequestMethod.GET)
    public Map<String, Object> getUnrelatedMethod(@RequestParam(value = "filter", required = false) String filterText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(0, 10, filterText, null,
                objectMapper);

        List<Map<String, String>> list =  methodService.getUnrelatedMethods(queryFilter);
        return CommonDataFormatTool.formatListResult((long) list.size(), list);
    }
}

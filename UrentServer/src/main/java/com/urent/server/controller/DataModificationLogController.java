package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.DataModificationLog;
import com.urent.server.service.DataModificationLogService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * DataModificationLogController
 * <p/>
 * Created by Administrator on 2015/8/22
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class DataModificationLogController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DataModificationLogService dataModificationLogService;

    /**
     * 查询DataModificationLog对象列表
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
    @RequestMapping(value = "/dataModificationLog", method = RequestMethod.GET)
    public Map<String, Object> getDataModificationLogs(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                                       @RequestParam(value = "filter", required = false) String filterText,
                                                       @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<DataModificationLog> list = dataModificationLogService.getDataModificationLogs(queryFilter);
        Long total = dataModificationLogService.getDataModificationLogCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询DataModificationLog对象
     *
     * @param id
     * @return DataModificationLog对象
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/dataModificationLog/{id}", method = RequestMethod.GET)
    public DataModificationLog getDataModificationLogById(@PathVariable(value = "id") long id) {
        return dataModificationLogService.getDataModificationLogById(id);
    }

}

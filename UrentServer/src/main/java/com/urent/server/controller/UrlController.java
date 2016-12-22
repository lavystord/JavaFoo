package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.Url;
import com.urent.server.service.UrlService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * UrlController
 * <p/>
 * Created by Administrator on 2015/8/19
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class UrlController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UrlService urlService;

    /**
     * 查询Url对象列表
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
    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public Map<String, Object> getUrls(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                       @RequestParam(value = "filter", required = false) String filterText,
                                       @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<Url> list = urlService.getUrls(queryFilter);
        Long total = urlService.getUrlCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询Url对象
     *
     * @param id
     * @return Url对象
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/url/{id}", method = RequestMethod.GET)
    public Url getUrlById(@PathVariable(value = "id") long id) {
        return urlService.getUrlById(id);
    }

    /**
     * 增加Url对象
     *
     * @param url
     * @return Url对象
     */
    @RequestMapping(value = "/url", method = RequestMethod.POST)
    public Url addUrl(@RequestBody @Valid Url url) {
        return urlService.addUrl(url);
    }


    /**
     * 更新Url对象
     *
     * @param url
     * @param id
     * @return Url对象
     */
    @RequestMapping(value = "/url/{id}", method = RequestMethod.PUT)
    public Url updateUrl(@RequestBody Url url, @PathVariable("id") long id) {
        urlService.updateUrl(url);
        return url;
    }

    /**
     * 删除Url对象
     *
     * @param url
     * @return {
     * success: true
     * }
     */
    @RequestMapping(value = "/url/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteUrl(Url url) {
        urlService.deleteUrl(url);
        return SuccessNoDataResult.getSuccessResult();
    }
}

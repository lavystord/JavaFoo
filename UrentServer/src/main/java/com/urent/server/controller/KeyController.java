package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.Key;
import com.urent.server.domain.User;
import com.urent.server.service.KeyService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.GlobalConstant;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * KeyController
 * <p/>
 * Created by Administrator on 2015/8/16
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class KeyController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KeyService keyService;

    /**
     * 查询Key对象列表
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
    @RequestMapping(value = "/key", method = RequestMethod.GET)
    public Map<String, Object> getKeys(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                       @RequestParam(value = "filter", required = false) String filterText,
                                       @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<Key> list = keyService.getKeys(queryFilter);
        Long total = keyService.getKeyCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询Key对象
     *
     * @param id
     * @return Key对象
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/key/{id}", method = RequestMethod.GET)
    public Key getKeyById(@PathVariable(value = "id") long id) {
        return keyService.getKeyById(id);
    }

    /**
     * 增加Key对象
     *
     * @param key
     * @return Key对象
     */
    @RequestMapping(value = "/key", method = RequestMethod.POST)
    public Key addKey(@RequestBody @Valid Key key) {
        return keyService.addKey(key);
    }

    @RequestMapping(value = "/key/deactivate", method = RequestMethod.POST)
    public Map<String, Boolean> deactivateKey(@RequestBody Key key, HttpSession session) {
        User user = (User) session.getAttribute(GlobalConstant.userConstant);
        keyService.deactivateKey(key, user);
        return SuccessNoDataResult.getSuccessResult();
    }

    @RequestMapping(value = "/key/activate", method = RequestMethod.POST)
    public Map<String, Boolean> activateKey(@RequestBody Key key, HttpSession session) {
        key.setUpdateDate(new Date());
        User user = (User) session.getAttribute(GlobalConstant.userConstant);
        keyService.activateKey(key, user);
        return SuccessNoDataResult.getSuccessResult();
    }

    /**
     * 更新Key对象
     *
     * @param key
     * @param id
     * @return Key对象
    @RequestMapping(value = "/key/{id}", method = RequestMethod.PUT)
    public Key updateKey(@RequestBody Key key, @PathVariable("id") long id) {
        keyService.updateKey(key);
        return key;
    }
     */

    /**
     * 删除Key对象
     *
     * @param key
     * @return {
     * success: true
     * }
    @RequestMapping(value = "/key/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteKey(Key key) {
        keyService.deleteKey(key);
        return SuccessNoDataResult.getSuccessResult();
    }
     */
}

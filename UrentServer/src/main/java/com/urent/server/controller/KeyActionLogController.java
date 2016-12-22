package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.KeyActionLog;
import com.urent.server.domain.User;
import com.urent.server.service.KeyActionLogService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.GlobalConstant;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * KeyActionLogController
 * <p/>
 * Created by Administrator on 2015/8/25
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class KeyActionLogController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    KeyActionLogService keyActionLogService;

    /**
     * 查询KeyActionLog对象列表
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
    @RequestMapping(value = "/keyActionLog", method = RequestMethod.GET)
    public Map<String, Object> getKeyActionLogs(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                                @RequestParam(value = "filter", required = false) String filterText,
                                                @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<KeyActionLog> list = keyActionLogService.getKeyActionLogs(queryFilter);
        Long total = keyActionLogService.getKeyActionLogCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询KeyActionLog对象
     *
     * @param id
     * @return KeyActionLog对象
     */
    @RequestMapping(value = "/keyActionLog/{id}", method = RequestMethod.GET)
    public KeyActionLog getKeyActionLogById(@PathVariable(value = "id") long id) {
        return keyActionLogService.getKeyActionLogById(id);
    }


    /*@RequestMapping(value = "/myKey/keyUsedLog", method = RequestMethod.GET)
    public Map<String, Object> getKeyUsedLogs(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                              @RequestParam(value = "keyId")Long keyId,
                                                @RequestParam(value = "filter", required = false) String filterText,
                                                @RequestParam(value = "sort", required = false) String sortText,
                                              HttpSession session) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        User user = (User) session.getAttribute(GlobalConstant.userConstant);
        List<KeyActionLog> list = keyActionLogService.getKeyUsedLogs(keyId, user, queryFilter);
        Long total = 0L;            // 这个查询暂时不支持total


        return CommonDataFormatTool.formatListResult(total, list);
    }*/

    /**
     * 增加KeyActionLog对象
     *
     * @param keyActionLog
     * @return KeyActionLog对象
    @RequestMapping(value = "/keyActionLog", method = RequestMethod.POST)
    public KeyActionLog addKeyActionLog(@RequestBody @Valid KeyActionLog keyActionLog) {
        return keyActionLogService.addKeyActionLog(keyActionLog);
    }

     */

    /**
     * 更新KeyActionLog对象
     *
     * @param keyActionLog
     * @param id
     * @return KeyActionLog对象
    @RequestMapping(value = "/keyActionLog/{id}", method = RequestMethod.PUT)
    public KeyActionLog updateKeyActionLog(@RequestBody KeyActionLog keyActionLog, @PathVariable("id") long id) {
        keyActionLogService.updateKeyActionLog(keyActionLog);
        return keyActionLog;
    }
     */

    /**
     * 删除KeyActionLog对象
     *
     * @param keyActionLog
     * @return {
     * success: true
     * }
    @RequestMapping(value = "/keyActionLog/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteKeyActionLog(KeyActionLog keyActionLog) {
        keyActionLogService.deleteKeyActionLog(keyActionLog);
        return SuccessNoDataResult.getSuccessResult();
    }
     */
}

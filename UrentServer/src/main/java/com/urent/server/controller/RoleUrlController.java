package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.RoleUrl;
import com.urent.server.service.RoleUrlService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.urent.server.domain.View;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * RoleUrlController
 * <p/>
 * Created by Administrator on 2015/8/20
 * Using template "SpringMvcController" created by Xc for project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@RestController
public class RoleUrlController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RoleUrlService roleUrlService;

    /**
     * 查询RoleUrl对象列表
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
    @RequestMapping(value = "/roleUrl", method = RequestMethod.GET)
    public Map<String, Object> getRoleUrls(@RequestParam(value = "start") Integer start, @RequestParam(value = "limit") Integer limit,
                                           @RequestParam(value = "filter", required = false) String filterText,
                                           @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<RoleUrl> list = roleUrlService.getRoleUrls(queryFilter);
        Long total = roleUrlService.getRoleUrlCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }


    /**
     * 查询RoleUrl对象
     *
     * @param id
     * @return RoleUrl对象
     */
    @JsonView(View.Detail.class)
    @RequestMapping(value = "/roleUrl/{id}", method = RequestMethod.GET)
    public RoleUrl getRoleUrlById(@PathVariable(value = "id") long id) {
        return roleUrlService.getRoleUrlById(id);
    }

    /**
     * 增加RoleUrl对象
     *
     * @param roleUrl
     * @return RoleUrl对象
     */
    @RequestMapping(value = "/roleUrl", method = RequestMethod.POST)
    public RoleUrl addRoleUrl(@RequestBody @Valid RoleUrl roleUrl) {
        return roleUrlService.addRoleUrl(roleUrl);
    }


    /**
     * 更新RoleUrl对象
     *
     * @param roleUrl
     * @param id
     * @return RoleUrl对象
     */
    @RequestMapping(value = "/roleUrl/{id}", method = RequestMethod.PUT)
    public RoleUrl updateRoleUrl(@RequestBody RoleUrl roleUrl, @PathVariable("id") long id) {
        roleUrlService.updateRoleUrl(roleUrl);
        return roleUrl;
    }

    /**
     * 删除RoleUrl对象
     *
     * @param roleUrl
     * @return {
     * success: true
     * }
     */
    @RequestMapping(value = "/roleUrl/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteRoleUrl(RoleUrl roleUrl) {
        roleUrlService.deleteRoleUrl(roleUrl);
        return SuccessNoDataResult.getSuccessResult();
    }
}

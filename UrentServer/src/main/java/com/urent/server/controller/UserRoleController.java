package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.UserRole;
import com.urent.server.domain.View;
import com.urent.server.service.UserRoleService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
@RestController
public class UserRoleController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRoleService userRoleService;


    @JsonView(View.Least.class)
    @RequestMapping(value = "/userRole", method = RequestMethod.GET)
    public Map<String, Object> getUserRoles(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<UserRole> list = userRoleService.getUserRoles(queryFilter);
        Long total = userRoleService.getUserRoleCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }

    @RequestMapping(value = "/userRole", method = RequestMethod.POST)
    public UserRole addUserRole(@RequestBody @Valid UserRole userRole) {
        return userRoleService.addUserRole(userRole);
    }

    @RequestMapping(value = "/userRole/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteUserRole(@RequestBody @Valid UserRole userRole, @PathVariable("id")Long id) {
        userRoleService.deleteUserRole(userRole);
        return SuccessNoDataResult.getSuccessResult();
    }
}

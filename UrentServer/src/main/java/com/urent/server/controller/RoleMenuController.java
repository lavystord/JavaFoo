package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.RoleMenu;
import com.urent.server.domain.UserRole;
import com.urent.server.domain.View;
import com.urent.server.service.RoleMenuService;
import com.urent.server.service.UserRoleService;
import com.urent.server.util.CommonDataFormatTool;
import com.urent.server.util.SuccessNoDataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/9.
 */
@RestController
public class RoleMenuController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RoleMenuService roleMenuService;


    @JsonView(View.Least.class)
    @RequestMapping(value = "/roleMenu", method = RequestMethod.GET)
    public Map<String, Object> getRoleMenus(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<RoleMenu> list = roleMenuService.getRoleMenus(queryFilter);
        Long total = roleMenuService.getRoleMenuCount(queryFilter);

        return CommonDataFormatTool.formatListResult(total, list);
    }

    @RequestMapping(value = "/roleMenu/{id}", method = RequestMethod.PUT)
    public RoleMenu updateRoleMenu(@PathVariable("id")Long id, @RequestBody RoleMenu roleMenu) {
        return roleMenuService.updateRoleMenu(roleMenu);
    }


    @RequestMapping(value = "/roleMenu/{id}", method = RequestMethod.DELETE)
    public Map<String, Boolean> deleteRoleMenu(@PathVariable("id")Long id, @RequestBody RoleMenu roleMenu) {
        roleMenuService.deleteRoleMenu(roleMenu);
        return SuccessNoDataResult.getSuccessResult();
    }

    @RequestMapping(value = "/roleMenu", method = RequestMethod.POST)
    public RoleMenu addRoleMenu(@RequestBody RoleMenu roleMenu) {
        return roleMenuService.addRoleMenu(roleMenu);
    }
}

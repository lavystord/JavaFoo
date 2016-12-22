package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.Role;
import com.urent.server.domain.UserRole;
import com.urent.server.domain.View;
import com.urent.server.service.RoleService;
import com.urent.server.util.CommonDataFormatTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
@RestController
public class RoleController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RoleService roleService;


    @JsonView({View.Summary.class})
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Map<String, Object> getRoles(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = false)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<Role> list = roleService.getRoles(queryFilter);
        Long total = roleService.getRoleCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }

    @JsonView(View.Detail.class)
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Role getRoleById(@PathVariable(value = "id")long id) {
        return roleService.getRoleById(id);
    }


    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public Role addRole(@RequestBody @Valid Role role) {
        return roleService.addRole(role);
    }


    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Role updateRole(@RequestBody Role role, @PathVariable("id") long id) {
        roleService.updateRole(role);
        return role;
    }

    @JsonView({View.Summary.class})
    @RequestMapping(value = "/roleUnrelated", method = RequestMethod.GET)
    public Map<String, Object> getUnrelatedRoles(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "filter", required = true)String filterText,
                                            @RequestParam(value = "sort", required = false) String sortText){
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);

        List<Role> list = roleService.getUnrelatedRoles(queryFilter);
        Long total = roleService.getUnrelatedRoleCount(queryFilter);


        return CommonDataFormatTool.formatListResult(total, list);
    }

}

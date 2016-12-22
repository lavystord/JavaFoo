package com.urent.server.service;

import com.urent.server.domain.Role;
import com.urent.server.domain.RoleMenu;
import com.urent.server.persistence.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/9.
 */
@Service
public class RoleMenuService {

    @Autowired
    RoleMenuMapper roleMenuMapper;

    public List<RoleMenu> getRoleMenus(Map<String, Object> queryFilter) {
        return roleMenuMapper.getRoleMenus(queryFilter);
    }

    public Long getRoleMenuCount(Map<String, Object> queryFilter) {
        return roleMenuMapper.getRoleMenuCount(queryFilter);
    }

    public RoleMenu updateRoleMenu(RoleMenu roleMenu) {
        roleMenuMapper.updateRoleMenu(roleMenu);
        return roleMenu;
    }

    public RoleMenu addRoleMenu(RoleMenu roleMenu) {
        roleMenuMapper.addRoleMenu(roleMenu);
        return roleMenu;
    }

    public void deleteRoleMenu(RoleMenu roleMenu) {
        roleMenuMapper.deleteRoleMenu(roleMenu);
    }
}

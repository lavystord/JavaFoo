package com.urent.server.service;

import com.urent.server.domain.Role;
import com.urent.server.domain.UserRole;
import com.urent.server.persistence.RoleMapper;
import com.urent.server.persistence.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
@Service
public class RoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RoleMapper roleMapper;

    public List<Role> getRoles(Map<String, Object> queryFilter){
        return roleMapper.getRoles(queryFilter);
    }

    public List<Role> getUnrelatedRoles(Map<String, Object> queryFilter) {
        return roleMapper.getUnrelatedRoles(queryFilter);
    }

    public Long getUnrelatedRoleCount(Map<String, Object> queryFilter) {
        return roleMapper.getUnrelatedRoleCount(queryFilter);
    }

    public Role getRoleById(long id) {
        return roleMapper.getRoleById(id);
    }

    public Long getRoleCount(Map<String, Object> queryFilter) {
        return roleMapper.getRoleCount(queryFilter);
    }

    public Role addRole(Role role) {
        roleMapper.addRole(role);
        return role;
    }

    public void updateRole(Role role) {
        roleMapper.updateRole(role);
        return;
    }
}

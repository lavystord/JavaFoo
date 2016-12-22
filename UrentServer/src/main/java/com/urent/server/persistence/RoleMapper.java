package com.urent.server.persistence;

import com.urent.server.domain.Role;
import com.urent.server.domain.User;
import com.urent.server.domain.UserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
public interface RoleMapper {
    public List<Role> getRoles(Map<String, Object> map);

    public Long getRoleCount(Map<String, Object> map);

    public List<Role> getUnrelatedRoles(Map<String, Object> map);

    public Long getUnrelatedRoleCount(Map<String, Object> map);

    public int addRole(Role role);

    public void updateRole(Role role);

    public Role getRoleById(long id);

    public List<Role> getRolesByUser(User user);
}

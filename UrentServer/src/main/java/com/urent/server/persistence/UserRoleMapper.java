package com.urent.server.persistence;

import com.urent.server.domain.UserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
public interface UserRoleMapper {
    public List<UserRole> getUserRoles(Map<String, Object> map);

    public Long getUserRoleCount(Map<String, Object> map);

    public int addUserRole(UserRole userRole);

    public void deleteUserRole(UserRole userRole);
}

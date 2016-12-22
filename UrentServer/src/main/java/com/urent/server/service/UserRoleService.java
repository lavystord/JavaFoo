package com.urent.server.service;

import com.urent.server.domain.UserRole;
import com.urent.server.persistence.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/6.
 */
@Service
public class UserRoleService {

    @Autowired
    UserRoleMapper userRoleMapper;

    public List<UserRole> getUserRoles(Map<String, Object> queryFilter){
        return userRoleMapper.getUserRoles(queryFilter);
    }


    public Long getUserRoleCount(Map<String, Object> queryFilter) {
        return userRoleMapper.getUserRoleCount(queryFilter);
    }


    public UserRole addUserRole(UserRole userRole) {
        userRoleMapper.addUserRole(userRole);
        return userRole;
    }

    public void deleteUserRole(UserRole userRole) {
        userRoleMapper.deleteUserRole(userRole);
    }
}

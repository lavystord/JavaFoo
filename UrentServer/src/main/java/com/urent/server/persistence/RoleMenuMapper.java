package com.urent.server.persistence;

import com.urent.server.domain.RoleMenu;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/9.
 */
public interface RoleMenuMapper {
    public List<RoleMenu> getRoleMenus(Map<String, Object> map);

    public Long getRoleMenuCount(Map<String, Object> map);

    public void updateRoleMenu(RoleMenu roleMenu);

    public int addRoleMenu(RoleMenu roleMenu);

    public void deleteRoleMenu(RoleMenu roleMenu);
}

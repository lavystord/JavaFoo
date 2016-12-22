package com.urent.server.persistence;

import com.urent.server.domain.Acl;
import com.urent.server.domain.AppMenu;
import com.urent.server.domain.Menu;
import com.urent.server.domain.Role;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-5
 * Time: 下午1:30
 * To change this template use File | Settings | File Templates.
 */
public interface MenuMapper {
    List<Menu> getAllMenus();

    List<AppMenu> getLeafMenusByRoles(List<Role> roles);

    AppMenu getMenuById(Long id);

    List<Acl> getAclsByMenuAndRole(Map map);

    List<Menu> getUnrelatedMenus(Map map);

    Long getUnrelatedMenuCount(Map map);
}

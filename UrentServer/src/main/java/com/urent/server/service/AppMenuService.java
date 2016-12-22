package com.urent.server.service;

import com.urent.server.domain.*;
import com.urent.server.persistence.MenuMapper;
import com.urent.server.persistence.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-11
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AppMenuService {

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    RoleMapper roleMapper;

    private void setMenuAcl(AppMenu appMenu, List<Role>roles) {
        if(appMenu.isLeaf()){
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("menu", appMenu);
            map2.put("roles", roles);
            List<Acl> acls = menuMapper.getAclsByMenuAndRole(map2);
            Acl acl = new Acl();
            acl.setRead(true);
            acl.setCreate(false);
            acl.setUpdate(false);
            acl.setDeletee(false);

            Iterator<Acl> aclIterator = acls.iterator();
            while (aclIterator.hasNext()) {
                Acl acl1 = aclIterator.next();

                if(acl1.isCreate())
                    acl.setCreate(true);

                if(acl1.isDeletee())
                    acl.setDeletee(true);

                if(acl1.isUpdate())
                    acl.setUpdate(true);
            }
            appMenu.setAcl(acl);
        }
    }

    private List<AppMenu> populateAppMenu(User user) {

        List<Role> roles = null;
        if(user != null)
            roles = roleMapper.getRolesByUser(user);
        List<AppMenu> appMenus = menuMapper.getLeafMenusByRoles(roles);

        Map<Long, AppMenu> appMenuMap = new HashMap<Long, AppMenu>();
        Iterator<AppMenu>iterator = appMenus.iterator();
        while (iterator.hasNext()) {
            AppMenu appMenu = iterator.next();

            // 用一个深度优先遍历的方法把所有的父亲结点遍历，并放入map里
            do{
                if(appMenuMap.get(appMenu.getId()) != null)
                    break;

                // 根据菜单和角色来决定访问权限
                if(roles != null)
                    setMenuAcl(appMenu, roles);

                appMenuMap.put(appMenu.getId(), appMenu);

                if(appMenu.getParentId() != null) {
                    AppMenu parent = appMenuMap.get(appMenu.getParentId());
                    if(parent != null) {
                        parent.getChildren().add(appMenu);
                        break;
                    }
                    else {
                        parent = menuMapper.getMenuById(appMenu.getParentId());
                        List<AppMenu> children = new ArrayList<AppMenu>();
                        children.add(appMenu);
                        parent.setChildren(children);
                        appMenu = parent;
                    }
                }
                else
                    break;
            }while (true);
        }

        appMenus = new ArrayList<AppMenu>();
        Iterator it = appMenuMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            AppMenu appMenu = (AppMenu) entry.getValue();
            if(appMenu.getParentId() == null)
                appMenus.add(appMenu);
        }
        return appMenus;

    }

    public List<AppMenu> getAppMenu(User user) {
        return populateAppMenu(user);
    }

    public List<AppMenu> getFullAppMenuWithoutAcl() {
        return  populateAppMenu(null);
    }


    public List<Menu> getUnrelatedMenus(Map<String, Object> queryFilter) {
        return menuMapper.getUnrelatedMenus(queryFilter);
    }


    public Long getUnrelatedMenuCount(Map<String, Object> queryFilter) {
        return menuMapper.getUnrelatedMenuCount(queryFilter);
    }
}

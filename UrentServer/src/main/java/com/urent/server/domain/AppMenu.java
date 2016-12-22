package com.urent.server.domain;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-11
 * Time: 下午1:31
 * To change this template use File | Settings | File Templates.
 */
public class AppMenu extends Menu{

    List<AppMenu> children;
    Acl acl;


    public List<AppMenu> getChildren() {
        return children;
    }

    public void setChildren(List<AppMenu> children) {
        this.children = children;
    }

    public Acl getAcl() {
        return acl;
    }

    public void setAcl(Acl acl) {
        this.acl = acl;
    }
}

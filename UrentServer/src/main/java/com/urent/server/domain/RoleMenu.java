package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-5
 * Time: 下午7:21
 * To change this template use File | Settings | File Templates.
 */
public class RoleMenu implements Serializable {
    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Role role;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Menu menu;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Boolean read = true;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Boolean create;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Boolean update;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Boolean deletee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getDeletee() {
        return deletee;
    }

    public void setDeletee(Boolean deletee) {
        this.deletee = deletee;
    }
}

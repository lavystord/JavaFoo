package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/7/31.
 */
public class UserRole {

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    User user;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class})
    Role role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

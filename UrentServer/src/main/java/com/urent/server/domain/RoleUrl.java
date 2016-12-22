package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/8/19.
 */
public class RoleUrl {
    public static final Long ROLE_ANY = -1L;                    // 任意角色
    public static final Long ROLE_UNLIMITED = -2L;              // 无限制（非登录用户也可以访问）

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    Long id;

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    Role role;

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    Url url;

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    String method;

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    String comment;


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

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

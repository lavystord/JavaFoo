package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-17
 * Time: 下午6:25
 * To change this template use File | Settings | File Templates.
 */
public class Role implements Serializable {
    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    Long id;

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    @NotNull(message = "角色名不能为空")
    String name;


    @JsonView({View.Detail.class, View.Summary.class})
    String comment;

    @JsonView({View.Detail.class, View.Summary.class})
    @NotNull(message = "角色是否活跃必须被明确")
    boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

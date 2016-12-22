package com.urent.server.domain;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2015/8/3.
 */
public class Subsystem {
    Long id;

    @NotNull
    String name;

    @NotNull
    String password;

    @NotNull
    String description;

    @NotNull
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

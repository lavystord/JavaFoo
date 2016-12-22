package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-18
 * Time: 下午2:07
 * To change this template use File | Settings | File Templates.
 */
public class Menu implements Serializable{
    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    Long id;

    @JsonView({View.Detail.class, View.Summary.class, View.Least.class})
    String name;

    @JsonView({View.Detail.class, View.Summary.class})
    String dest;

    @JsonView({View.Detail.class, View.Summary.class})
    boolean leaf;

    @JsonView({View.Detail.class, View.Summary.class})
    Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

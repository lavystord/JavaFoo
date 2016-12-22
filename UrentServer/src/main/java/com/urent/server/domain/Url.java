package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/8/19.
 */
public class Url {

    @JsonView({View.Summary.class, View.Detail.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class})
    String value;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

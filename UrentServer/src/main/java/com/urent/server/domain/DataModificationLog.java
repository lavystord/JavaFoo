package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/22.
 */
public class DataModificationLog {

    @JsonView({View.Summary.class, View.Detail.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class})
    User user;

    @JsonView({View.Summary.class, View.Detail.class})
    String calledClassName;

    @JsonView({View.Summary.class, View.Detail.class})
    String calledMethodName;

    @JsonView({View.Summary.class, View.Detail.class})
    String method;

    @JsonView({View.Summary.class, View.Detail.class})
    String remoteAddress;

    @JsonView({View.Summary.class, View.Detail.class})
    String userAgent;

    @JsonView({View.Summary.class, View.Detail.class})
    String bodyData;

    @JsonView({View.Summary.class, View.Detail.class})
    Date createDate;

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

    public String getCalledClassName() {
        return calledClassName;
    }

    public void setCalledClassName(String calledClassName) {
        this.calledClassName = calledClassName;
    }

    public String getCalledMethodName() {
        return calledMethodName;
    }

    public void setCalledMethodName(String calledMethodName) {
        this.calledMethodName = calledMethodName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getBodyData() {
        return bodyData;
    }

    public void setBodyData(String bodyData) {
        this.bodyData = bodyData;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

package com.urent.server.domain.util;

/**
 * Created by Administrator on 2015/8/4.
 */
public class QueryFilter {
    String property;

    Object value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

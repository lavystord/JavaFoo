package com.urent.server.domains;

import java.util.List;

/**
 * Created by Administrator on 2015/8/24.
 */
public class GetListResult <T> {
    Long total;

    List<T> list;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}

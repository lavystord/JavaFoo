package com.urent.server.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-4-11
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
public class Acl implements Serializable{
    boolean create;
    boolean read;
    boolean update;
    /*
     因为YUIJavascriptCompressor在压缩js文件时处理不好delete关键字，这里只好写成deletee
     否则执行sencha app build生成前台代码时会报错   by xc
     */
    boolean deletee;

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isDeletee() {
        return deletee;
    }

    public void setDeletee(boolean deletee) {
        this.deletee = deletee;
    }
}

package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

/**
 * Created by Administrator on 2015/8/25.
 */
public class KeyActionLog {
    public final static short ACTION_GET_DISPOSABLE_KEYWORD = 0;
    public final static short ACTION_GET_CONSTANT_KEYWORD = 1;
    public final static short ACTION_UNLOCK = 2;
    public final static short ACTION_DFU_COMPLETE = 3;
    public final static short ACTION_LEAVE = 4;

    public final static short ACTION_DEACTIVATE = 100;
    public final static short ACTION_DEACTIVATE_CHAIN = 101;
    public final static short ACTION_ACTIVATE = 102;


    @JsonView({View.KeyLogResult.class})
    Long id;

    @JsonView({View.KeyLogResult.class})
    Key key;

    Lock lock;

    User manager;           // 动作的操作员

    String lockGapAddress;

    House house;

    short action;

    String data;

    @JsonView({View.KeyLogResult.class})
    Date time;

    Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public short getAction() {
        return action;
    }

    public void setAction(short action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLockGapAddress() {
        return lockGapAddress;
    }

    public void setLockGapAddress(String lockGapAddress) {
        this.lockGapAddress = lockGapAddress;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}

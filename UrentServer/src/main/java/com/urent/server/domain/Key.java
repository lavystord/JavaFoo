package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/7/29.
 */
public class Key {
    public final static String typePrimary = "primary";
    public final static String typeSlave = "slave";
    public final static String typeTemp = "temp";

    public final static int statusActive = 1;           // 钥匙可用
    public final static int statusInUse = 2;            // 临时钥匙在使用后、确认离开前的状态
    public final static int statusInUseAndOverTime = 3;   // 临时钥匙使用后未确认离开，直到超时
    public final static int statusLent = 4;                 // 主钥匙暂时失效
    public final static int statusInactive = 0;             // 钥匙永久性失效
    public final static int statusExpiredButNotUse = -1; //临时钥匙未使用就过期
    public final static int statusExpiredAndUserCheck = -2; //临时钥匙过期后，用户自己确认离开
    public final static int statusExpiredAndAdminCheck = -3; //临时钥匙过期后，管理员后台清除

    public static boolean isKeyValid(Key key) {
        return key.status > 0;
    }

    @JsonView({View.Summary.class, View.Detail.class,
            View.MyKeyDetail.class,View.MyKeyList.class,
            View.ShareDetail.class,View.ShareResult.class,
            View.KeyLogResult.class
    })
    Long id;

    @NotNull
    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class,View.ShareResult.class})
    Lock lock;

    @NotNull
    @JsonView({View.Summary.class, View.Detail.class,
            View.MyKeyDetail.class,View.ShareDetail.class,
            View.ShareResult.class, View.KeyLogResult.class})
    User owner;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.ShareResult.class})
    Key sharedFrom;


    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyList.class, View.MyKeyDetail.class,View.ShareDetail.class,View.ShareResult.class})
    Date expiredDate;

//    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.ShareDetail.class,View.ShareResult.class})
    Integer maxTimes;

//    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.ShareDetail.class,View.ShareResult.class})
    Integer usedTimes;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class,View.ShareResult.class})
    String alias;

    @JsonView({View.Summary.class, View.Detail.class,View.MyKeyDetail.class,View.MyKeyList.class,View.ShareResult.class})
    Integer status;

    @JsonView({View.Summary.class, View.Detail.class})
    Integer maxSharedCount;                 // 最大可分享数目，以后用

    @JsonView({View.Summary.class, View.Detail.class,
            View.MyKeyDetail.class,View.MyKeyList.class,
            View.ShareResult.class, View.KeyLogResult.class})
    String type;                               // 类型（primary, slave,  temp }

    @JsonView({View.Summary.class, View.Detail.class,View.ShareResult.class})
    Date createDate;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class,View.ShareResult.class})
    Date updateDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }


    public Key getSharedFrom() {
        return sharedFrom;
    }

    public void setSharedFrom(Key sharedFrom) {
        this.sharedFrom = sharedFrom;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public Integer getMaxTimes() {
        return maxTimes;
    }

    public void setMaxTimes(Integer maxTimes) {
        this.maxTimes = maxTimes;
    }

    public Integer getUsedTimes() {
        return usedTimes;
    }

    public void setUsedTimes(Integer usedTimes) {
        this.usedTimes = usedTimes;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getMaxSharedCount() {
        return maxSharedCount;
    }

    public void setMaxSharedCount(Integer maxSharedCount) {
        this.maxSharedCount = maxSharedCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}

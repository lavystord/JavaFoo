package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/29.
 */
public class Lock {
    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class,View.ShareResult.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    House house;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    LockType type;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    String gapAddress;

    @JsonView({View.Summary.class, View.Detail.class})
    Boolean active;

    String disposableEncryptWord;

    String constantEncryptWord;

    @JsonView({View.MyKeyDetail.class,View.MyKeyList.class})
    String constantKeyWord;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    Date constantKeyWordExpiredDate;

    @JsonView({View.Summary.class, View.Detail.class})
    Version currentFirmwareVersion;

    @JsonView({View.Summary.class, View.Detail.class})
    Integer powerDensity=80;

    public Integer getPowerDensity() {
        return powerDensity;
    }

    public void setPowerDensity(Integer powerDensity) {
        this.powerDensity = powerDensity;
    }

    Date createDate;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public LockType getType() {
        return type;
    }

    public void setType(LockType type) {
        this.type = type;
    }

    public String getGapAddress() {
        return gapAddress;
    }

    public void setGapAddress(String gapAddress) {
        this.gapAddress = gapAddress;
    }

    public String getDisposableEncryptWord() {
        return disposableEncryptWord;
    }

    public void setDisposableEncryptWord(String disposableEncryptWord) {
        this.disposableEncryptWord = disposableEncryptWord;
    }

    public String getConstantEncryptWord() {
        return constantEncryptWord;
    }

    public void setConstantEncryptWord(String constantEncryptWord) {
        this.constantEncryptWord = constantEncryptWord;
    }

    public String getConstantKeyWord() {
        return constantKeyWord;
    }

    public void setConstantKeyWord(String constantKeyWord) {
        this.constantKeyWord = constantKeyWord;
    }

    public Date getConstantKeyWordExpiredDate() {
        return constantKeyWordExpiredDate;
    }

    public void setConstantKeyWordExpiredDate(Date constantKeyWordExpiredDate) {
        this.constantKeyWordExpiredDate = constantKeyWordExpiredDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public Version getCurrentFirmwareVersion() {
        return currentFirmwareVersion;
    }

    public void setCurrentFirmwareVersion(Version currentFirmwareVersion) {
        this.currentFirmwareVersion = currentFirmwareVersion;
    }
}

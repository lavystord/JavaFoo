package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/7/29.
 */
public class Area {

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    String areaName;

    @JsonView({View.Detail.class, View.MyKeyDetail.class})
    Area parent;

    String shortName;

    Integer zipCode;

    String pinYin;

    Integer level;

    Integer sort;

    @JsonView({View.Summary.class, View.Detail.class})
    String areaString;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Area getParent() {
        return parent;
    }

    public void setParent(Area parent) {
        this.parent = parent;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getAreaString() {
        if(areaString == null) {
            if (parent != null) {
                areaString = parent.getAreaString() + areaName;
            } else {
                areaString = areaName;
            }
        }
        return areaString;
    }
}

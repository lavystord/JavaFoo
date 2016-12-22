package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/7/29.
 */
public class Address {

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class})
    Area area;

    @JsonView({View.Summary.class, View.Detail.class})
    String subdistrict;             // 小区名，如果没有小区名则用街道名

    @JsonView({View.Summary.class, View.Detail.class})
    Float longitude;

    @JsonView({View.Summary.class, View.Detail.class})
    Float latitude;

    @JsonView({View.Summary.class, View.Detail.class})
    String addressString;           // 生成的地址字符串


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getAddressString(){
        if(addressString == null) {
            if (area != null) {
                addressString = area.getAreaString() + subdistrict;
            } else {
                addressString = subdistrict;
            }
        }
        return addressString;
    }
}

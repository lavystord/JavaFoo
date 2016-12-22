package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/7/29.
 */
public class House {
    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    Address address;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    User owner;                      // 房屋主人

    @JsonView({View.Summary.class, View.Detail.class})
    String building;                // 幛号

    @JsonView({View.Summary.class, View.Detail.class})
    String unit;                    // 单元号

    @JsonView({View.Summary.class, View.Detail.class})
    Integer floor;                   // 楼层

    @JsonView({View.Summary.class, View.Detail.class})
    String number;                  // 门牌

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class})
    String inaccurateAddress;     // 非精确地址

    @JsonView({View.Summary.class, View.Detail.class})
    String houseString;           // 精确地址

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setInaccurateAddress(String inaccurateAddress) {
        this.inaccurateAddress = inaccurateAddress;
    }

    public String getInaccurateAddress() {
        if(inaccurateAddress == null && address != null) {
            inaccurateAddress = address.getSubdistrict();
           // inaccurateAddress="浙江省杭州市西湖区浙大路38号";
        }
        return inaccurateAddress;
    }

    public String getHouseString() {
        if(houseString == null && address != null) {
            houseString = address.getAddressString();
            if(building != null) {
                houseString += building;
                houseString += "-";
            }
            if(unit != null) {
                houseString += unit;
                houseString += "-";
            }
            if(number != null) {
                houseString += number;
            }
        }
        return houseString;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}

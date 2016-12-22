package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.urent.server.USException;
import com.urent.server.persistence.KeyMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/29.
 */
public class Version {
    public final static Integer typeLockStructure = 1;
    public final static Integer typeLockFirmware = 2;
    public final static Integer typeAndroidApp = 101;
    public final static Integer typeIosApp = 201;



    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    short major;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    short minor;

    @JsonView({View.Detail.class})
    String firmwareFileId;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    Integer intFormat;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    String stringFormat;

    String comment;

    Date createDate;

    @JsonView({View.Summary.class, View.Detail.class})
    Integer type;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getMajor() {
        return major;
    }

    public void setMajor(short major) {
        this.major = major;
    }

    public short getMinor() {
        return minor;
    }

    public void setMinor(short minor) {
        this.minor = minor;
    }

    public String getFirmwareFileId() {
        return firmwareFileId;
    }

    public void setFirmwareFileId(String firmwareFileId) {
        this.firmwareFileId = firmwareFileId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public int getIntFormat() {
        if(intFormat == null){
            intFormat = (((int)major << 16) & 0xffff0000) | (minor & 0x0000ffff);
        }

        return intFormat;
    }


    public String getStringFormat() {
        if(stringFormat == null) {
            stringFormat = String.format("%d", major) + "." + String.format("%05d", minor);
        }
        return stringFormat;
    }

    public Version(String stringFormat) {
        this.stringFormat = stringFormat;
        short major=Short.parseShort(stringFormat.substring(0,stringFormat.indexOf(".")));
        short minor=Short.parseShort(stringFormat.substring(stringFormat.indexOf(".") + 1));
        this.major=major;
        this.minor=minor;
    }

    public Version(){}

    public int compareTo(Version version) {
        return getIntFormat() - version.getIntFormat();
    }

    public static void main(String[] args) {
        Version version = new Version();
        version.setMajor((short) 7);
        version.setMinor((short) 15);

        System.out.println(version.getIntFormat());
        System.out.println(version.getStringFormat());

        Version version1=new Version("12.0034");
        System.out.println(version1.getMajor());
        System.out.println(version1.getMinor());

        Version version2=new Version("5.0");
        System.out.println(version2.getMajor());
        System.out.println(version2.getMinor());

        USException.ErrorCode e1= USException.ErrorCode.PasswordVerifyError;
        System.out.println(e1.getCode());
        System.out.println(e1.getDefaultMessage());
        System.out.println(e1.toString());
        System.out.println(e1);
        System.out.println(e1.getClass());

    }

}

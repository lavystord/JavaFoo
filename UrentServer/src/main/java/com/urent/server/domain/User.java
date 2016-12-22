package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/23.
 */
public class User {
    public final static byte vs_unauthorized = 0;
    public final static byte vs_authorized = 1;
    public final static byte vs_certificating = 2;
    public final static byte vs_certificated = 3;


    @JsonView({View.Summary.class, View.Detail.class,
            View.Least.class,View.MyKeyDetail.class,View.QueryName.class,
            View.ShareDetail.class,View.ShareResult.class,
            View.KeyLogResult.class})
    Long id;

    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    @JsonView({View.Summary.class, View.Detail.class,View.MyKeyDetail.class,View.QueryName.class,View.ShareDetail.class,View.ShareResult.class})
    String mobile;

    @Size(max = 20, message = "姓名长度越界")
    @JsonView({View.Summary.class, View.Detail.class, View.Least.class,
            View.MyKeyDetail.class,View.QueryName.class,
            View.ShareDetail.class,View.ShareResult.class,
            View.KeyLogResult.class})
    String name;

    @Size(max = 20, message = "昵称长度越界")
    @JsonView({View.Summary.class, View.Detail.class, View.Least.class,
            View.MyKeyDetail.class,View.QueryName.class,
            View.ShareDetail.class,View.ShareResult.class,
            View.KeyLogResult.class})
    String nickname;

    @JsonView({View.Summary.class, View.Detail.class})
    String idCardNumber;

    @JsonView({View.Summary.class, View.Detail.class,View.MyKeyDetail.class})
    String gender;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 255, message = "密码长度应在6个字符到255个字符之间")
    String password;

    @JsonView({View.Detail.class})
    String verificationImageId;

    @JsonView({View.Summary.class, View.Detail.class, View.Least.class,View.MyKeyDetail.class,View.QueryName.class,View.ShareDetail.class,View.ShareResult.class})
    byte verificationStatus;// 0 means 未认证 1 means 实名校验 2 means 审核ing 3 means 实名认证

    @JsonView({View.Detail.class,View.MyKeyDetail.class,View.QueryName.class,View.ShareDetail.class,
            View.KeyLogResult.class})
    String headerImageId;

    @JsonView({View.Summary.class, View.Detail.class})
    String lastLoginDevice;

    @JsonView({View.Summary.class, View.Detail.class})
    Boolean active;

    @JsonView({View.Summary.class, View.Detail.class})
    Date createDate;

    @JsonView({View.Summary.class, View.Detail.class})
    Date updateDate;


    public final static byte messageRejectAll = 0;
    public final static byte messageReceiveAll = 1;


    @JsonView({View.Summary.class, View.Detail.class})
    byte messageOption;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeaderImageId() {
        return headerImageId;
    }

    public void setHeaderImageId(String headerImageId) {
        this.headerImageId = headerImageId;
    }

    public String getLastLoginDevice() {
        return lastLoginDevice;
    }

    public void setLastLoginDevice(String lastLoginDevice) {
        this.lastLoginDevice = lastLoginDevice;
    }

    public byte getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(byte verificationStatus) {
        this.verificationStatus = verificationStatus;
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


    public String getVerificationImageId() {
        return verificationImageId;
    }

    public void setVerificationImageId(String verificationImageId) {
        this.verificationImageId = verificationImageId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public byte getMessageOption() {
        return messageOption;
    }

    public void setMessageOption(byte messageOption) {
        this.messageOption = messageOption;
    }
}

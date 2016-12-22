package com.urent.server.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2015/9/10.
 */
public class UpdateMobileInfo {
    @NotNull(message = "验证码不能为空")
    Integer verificationCode;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6位")
    String password;


    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    String mobile;

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

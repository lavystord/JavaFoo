package com.urent.server.domain;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2015/7/31.
 */
public class UpdatePasswordInfo {
    @NotNull(message = "验证码不能为空")
    Integer verificationCode;

    @NotNull(message = "新密码不能为空")
    String newPassword;

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

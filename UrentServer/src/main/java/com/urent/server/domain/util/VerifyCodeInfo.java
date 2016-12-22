package com.urent.server.domain.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2015/9/16.
 */
public class VerifyCodeInfo {

    @NotNull(message = "验证码不能为空")
    Integer verificationCode;

    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    String mobile;

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

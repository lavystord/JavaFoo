package com.urent.server.domain;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2015/7/23.
 */
public class RegisterInfo {

    @Valid
    User user;

    @NotNull(message = "注册验证码不能为空")
    int registerCode;

    @NotNull(message = "登录设备名不能为空")
    String device;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(int registerCode) {
        this.registerCode = registerCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}

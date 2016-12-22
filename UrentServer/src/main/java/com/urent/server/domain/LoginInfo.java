package com.urent.server.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2015/7/26.
 */
public class LoginInfo {

    @NotNull(message = "登录名不能为空")
    String username;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 255, message = "密码长度应该在6个字符到255个字符之间")
    String password;

    @NotNull(message = "登录设备名不能为空")
    String device;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}

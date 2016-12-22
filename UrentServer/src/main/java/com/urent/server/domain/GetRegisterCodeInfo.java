package com.urent.server.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2015/7/27.
 */
public class GetRegisterCodeInfo {


    @NotNull(message = "手机号不能为空")
    @Size(min = 11, max = 11, message = "手机号长度必须为11位")
    String mobile;

    @NotNull(message = "必须指定是否检查唯一性")
    Boolean checkUnique;


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getCheckUnique() {
        return checkUnique;
    }

    public void setCheckUnique(Boolean checkUnique) {
        this.checkUnique = checkUnique;
    }
}

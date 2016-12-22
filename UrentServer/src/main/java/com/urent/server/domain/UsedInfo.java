package com.urent.server.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Dell on 2015/8/30.
 */

public class UsedInfo {
    @NotNull
    Long keyId;

    @NotNull
    String version;

    @NotNull
    Integer powerDensity;

    @NotNull
    Date time;

    public Integer getPowerDensity() {
        return powerDensity;
    }

    public void setPowerDensity(Integer powerDensity) {
        this.powerDensity = powerDensity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

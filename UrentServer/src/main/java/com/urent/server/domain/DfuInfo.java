package com.urent.server.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Administrator on 2015/10/23.
 */
public class DfuInfo {
    @NotNull
    Long keyId;

    @NotNull
    Version currentFirmwareVersion;

    @NotNull
    Date time;

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public Long getKeyId() {
        return keyId;
    }

    public Version getCurrentFirmwareVersion() {
        return currentFirmwareVersion;
    }

    public void setCurrentFirmwareVersion(Version currentFirmwareVersion) {
        this.currentFirmwareVersion = currentFirmwareVersion;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}

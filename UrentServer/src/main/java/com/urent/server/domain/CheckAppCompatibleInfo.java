package com.urent.server.domain;

/**
 * Created by Administrator on 2015/10/23.
 */
public class CheckAppCompatibleInfo {
    Boolean isCompatible;

    Boolean isNewest;

    Version newestVersion;

    public Boolean getIsCompatible() {
        return isCompatible;
    }

    public void setIsCompatible(Boolean isCompatible) {
        this.isCompatible = isCompatible;
    }

    public Boolean getIsNewest() {
        return isNewest;
    }

    public void setIsNewest(Boolean isNewest) {
        this.isNewest = isNewest;
    }

    public Version getNewestVersion() {
        return newestVersion;
    }

    public void setNewestVersion(Version newestVersion) {
        this.newestVersion = newestVersion;
    }
}

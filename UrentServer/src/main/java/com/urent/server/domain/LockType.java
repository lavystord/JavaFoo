package com.urent.server.domain;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * Created by Administrator on 2015/7/29.
 */
public class LockType {
    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    Long id;

    @JsonView({View.Summary.class, View.Detail.class})
    String name;

    @JsonView({View.Summary.class, View.Detail.class})
    Version hardwareVersion;

    @JsonView({View.Summary.class, View.Detail.class, View.MyKeyDetail.class,View.MyKeyList.class})
    Version newestFirmwareVersion;

    @JsonView({View.Summary.class, View.Detail.class})
    String description;

    @JsonView({View.Summary.class, View.Detail.class})
    String imageIds;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Version getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(Version hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Version getNewestFirmwareVersion() {
        return newestFirmwareVersion;
    }

    public void setNewestFirmwareVersion(Version newestFirmwareVersion) {
        this.newestFirmwareVersion = newestFirmwareVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageIds() {
        return imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds;
    }
}

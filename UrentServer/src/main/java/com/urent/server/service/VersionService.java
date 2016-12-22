package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.CheckAppCompatibleInfo;
import com.urent.server.domain.Version;
import com.urent.server.persistence.VersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
@Service
public class VersionService {

    @Autowired
    VersionMapper versionMapper;

    @Value("${minCompatibleAndroidVersion}")
    String minCompatibleAndroidVersion;

    @Value("${minCompatibleIosVersion}")
    String minCompatibleIosVersion;

    public List<Version> getVersions(Map<String, Object> queryFilters) {
        return versionMapper.getVersions(queryFilters);
    }

    public Long getVersionCount(Map<String, Object> queryFilters) {
        return versionMapper.getVersionCount(queryFilters);
    }

    public Version getVersionById(Long id) {
        return versionMapper.getVersionById(id);
    }

    public Version addVersion(Version version) {
        version.setCreateDate(new Date());
        versionMapper.addVersion(version);
        return version;
    }

    public Version updateVersion(Version version) {
        versionMapper.updateVersion(version);
        return version;
    }

    public CheckAppCompatibleInfo checkCompatible(String version, Integer type) {
        Version version1 = new Version(version);
        CheckAppCompatibleInfo ccInfo = new CheckAppCompatibleInfo();

        if(type.equals(Version.typeAndroidApp)) {
            Version minCompatibleVersion = new Version(minCompatibleAndroidVersion);
            if(minCompatibleVersion.compareTo(version1) > 0) {
                ccInfo.setIsCompatible(false);
            }
            else {
                ccInfo.setIsCompatible(true);
            }
        }
        else if(type.equals(Version.typeIosApp)){
            Version minCompatibleVersion = new Version(minCompatibleIosVersion);
            if(minCompatibleVersion.compareTo(version1) > 0) {
                ccInfo.setIsCompatible(false);
            }
            else {
                ccInfo.setIsCompatible(true);
            }
        }
        else {
            throw new USException(USException.ErrorCode.IllegalRequestParam, "提交的类型type不能被识别");
        }

        Map<String, Object> map = new HashMap<>(1);
        map.put("type", type);

        Version newestVersion = versionMapper.getNewestVersion(map);

        if(newestVersion == null) {
            throw new USException(USException.ErrorCode.NoSuchVersion, "未能找到相应类型的最新版本号");
        }

        if(version1.compareTo(newestVersion) < 0) {
            ccInfo.setIsNewest(false);
        }
        else {
            ccInfo.setIsNewest(true);
        }

        ccInfo.setNewestVersion(newestVersion);

        return ccInfo;
    }
}

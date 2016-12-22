package com.urent.server.persistence;

import com.urent.server.domain.Version;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public interface VersionMapper {
    public List<Version> getVersions(Map map);

    public Long getVersionCount(Map map);

    public Version getVersionById(long id);

    public Version getNewestVersion(Map map);

    public int addVersion(Version version);

    public void updateVersion(Version version);
}

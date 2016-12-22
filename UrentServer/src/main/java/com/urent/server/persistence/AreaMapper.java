package com.urent.server.persistence;

import com.urent.server.domain.Area;
import com.urent.server.domain.Version;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/13.
 */
public interface AreaMapper {
    public List<Area> getAreas(Map map);

    public Long getAreaCount(Map map);

    public Area getAreaWithAscendants(Long id);
}

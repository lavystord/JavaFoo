package com.urent.server.service;


import com.urent.server.domain.Area;
import com.urent.server.persistence.AreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/13.
 */
@Service
public class AreaService {
     @Autowired
     AreaMapper areaMapper;


    public List<Area> getAreas(Map<String, Object> queryFilters){
        return areaMapper.getAreas(queryFilters);
    }

    public Long getAreaCount(Map<String, Object> queryFilters) {
        return areaMapper.getAreaCount(queryFilters);
    }


}

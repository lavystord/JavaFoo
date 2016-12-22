package com.urent.server.persistence;

import com.urent.server.domain.House;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/15
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface HouseMapper {
    public List<House> getHouses(Map map);

    public Long getHouseCount(Map map);

    public House getHouseById(long id);

    public int addHouse(House house);

    public void updateHouse(House house);

    public void deleteHouse(House house);

    public Long getHouseReferenceCount(House house);
}

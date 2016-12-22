package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.House;
import com.urent.server.domain.Key;
import com.urent.server.domain.Lock;
import com.urent.server.persistence.AreaMapper;
import com.urent.server.persistence.HouseMapper;
import com.urent.server.persistence.KeyMapper;
import com.urent.server.persistence.LockMapper;
import com.urent.server.util.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * HouseService
 * Created by Administrator on 2015/8/15
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class HouseService {

    @Autowired
    HouseMapper houseMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    KeyMapper keyMapper;

    @Autowired
    LockMapper lockMapper;

    /**
     * 查询House对象列表
     *
     * @param queryFilters
     * @return
     */
    public List<House> getHouses(Map<String, Object> queryFilters) {
        return houseMapper.getHouses(queryFilters);
    }

    /**
     * 查询House数目
     *
     * @param queryFilters
     * @return
     */
    public Long getHouseCount(Map<String, Object> queryFilters) {
        return houseMapper.getHouseCount(queryFilters);
    }

    /**
     * 查询House对象
     *
     * @param id
     * @return
     */
    public House getHouseById(Long id) {
        House house = houseMapper.getHouseById(id);
        house.getAddress().setArea(areaMapper.getAreaWithAscendants(house.getAddress().getArea().getId()));
        return house;
    }

    /**
     * 增加House对象
     *
     * @param house
     * @return
     */
    public House addHouse(House house) {
        // 逻辑：判断一下阻止增加完全一样的房屋
        if(isHouseWithAddressExists(house)) {
            throw new USException(USException.ErrorCode.HouseWithSameAddressExists);
        }

        houseMapper.addHouse(house);
        return house;
    }

    /**
     * 更新House对象
     *
     * @param house
     * @return
     */
    @Transactional
    public House updateHouse(House house) {
        // 逻辑：如果要更新一个锁的房主，则必须保证原房主的钥匙已经先被撤除
        // 这里干脆严格点，该房必须没有Available的钥匙
        if(house.getOwner() != null) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("houseId", house.getId());
            if(keyMapper.getAvailableKeys(map).size() > 0) {
                throw new USException(USException.ErrorCode.AvailableKeyExistsOnLock);
            }
        }

        if(house.getAddress() != null || house.getBuilding() != null
                || house.getUnit() != null || house.getNumber() != null) {
            House house1 = houseMapper.getHouseById(house.getId());
            if(house.getAddress() != null) {
                house1.setAddress(house.getAddress());
            }
            if(house.getBuilding() != null) {
                house1.setBuilding(house.getBuilding());
            }
            if(house.getUnit() != null){
                house1.setUnit(house.getUnit());
            }
            if(house.getNumber() != null){
                house1.setNumber(house.getNumber());
            }

            if(isHouseWithAddressExists(house1)) {
                throw new USException(USException.ErrorCode.HouseWithSameAddressExists);
            }
        }

        houseMapper.updateHouse(house);

        // 逻辑：如果更新了一个房子的房主，则自动为其分配一把主钥匙
        if(house.getOwner() != null && house.getOwner().getId() != null){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("houseId", house.getId());
            List<Lock> list = lockMapper.getLocks(map);
            Iterator<Lock> iterator = list.iterator();
            while (iterator.hasNext()) {
                Lock lock = iterator.next();
                Key key;

               /* Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("lockId", lock.getId());
                map1.put("ownerId", house.getOwner().getId());
                map1.put("type", Key.typePrimary);
                List<Key> list1 = keyMapper.getKeys(map1);
                if(list1.size() > 0){
                    // 如果原来就有一把钥匙，则enable it
                    assert list1.size() == 1;
                    Key key1 = list1.get(0);
                    assert key1.getStatus() == Key.statusInactive;

                    key = new Key();
                    key.setId(key1.getId());
                    key.setStatus(Key.statusActive);
                    key.setUpdateDate(new Date());

                    keyMapper.updateKey(key);
                    return house;
                }
                else {*/
                // 否则创建一把新钥匙
                key = new Key();
                key.setLock(lock);
                key.setMaxSharedCount(GlobalConstant.maxSharedCountPerKey);
                key.setStatus(Key.statusActive);
                key.setOwner(house.getOwner());
                key.setType(Key.typePrimary);
                key.setCreateDate(new Date());
                key.setUpdateDate(new Date());

                if(key.getAlias() == null) {
                    Lock lock2 = lockMapper.getLockById(key.getLock().getId());
                    key.setAlias(lock2.getHouse().getInaccurateAddress());
                }

                keyMapper.addKey(key);
                /*}*/
            }
        }
        return house;
    }

    /**
     * 删除House对象
     *
     * @param house
     */
    public void deleteHouse(House house) {
        if(houseMapper.getHouseReferenceCount(house) > 0){
            throw new USException(USException.ErrorCode.DeleteReferentResource, "房屋有关联的锁，无法删除");
        }
        houseMapper.deleteHouse(house);
        return;
    }

    //todo 有问题
    private boolean isHouseWithAddressExists(House house) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("addressId", house.getAddress().getId());
        map.put("building", house.getBuilding());
        map.put("unit", house.getUnit());
        map.put("number", house.getNumber());

        return (houseMapper.getHouseCount(map) > 0L);
    }
}

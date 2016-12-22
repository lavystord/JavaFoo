package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.House;
import com.urent.server.domain.Key;
import com.urent.server.domain.Lock;
import com.urent.server.domain.Version;
import com.urent.server.persistence.*;
import com.urent.server.util.GlobalConstant;
import com.urent.server.util.LockWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * LockService
 * Created by Administrator on 2015/8/16
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class LockService {

    @Autowired
    LockMapper lockMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    KeyMapper keyMapper;

    @Autowired
    HouseMapper houseMapper;

    @Autowired
    VersionMapper versionMapper;
    /**
     * 查询Lock对象列表
     *
     * @param queryFilters
     * @return
     */
    public List<Lock> getLocks(Map<String, Object> queryFilters) {
        return lockMapper.getLocks(queryFilters);
    }

    /**
     * 查询Lock数目
     *
     * @param queryFilters
     * @return
     */
    public Long getLockCount(Map<String, Object> queryFilters) {
        return lockMapper.getLockCount(queryFilters);
    }

    /**
     * 查询Lock对象
     *
     * @param id
     * @return
     */
    public Lock getLockById(Long id) {
        Lock lock = lockMapper.getLockById(id);
        lock.getHouse().getAddress().setArea(areaMapper.getAreaWithAscendants(lock.getHouse().getAddress().getArea().getId()));
        return lock;
    }

    /**
     * 增加Lock对象
     *
     * @param lock
     * @return
     */
    @Transactional
    public Lock addLock(Lock lock) {
        // 逻辑，在增加一把锁时，要检查避免有同样GAP地址的（且活跃的）锁存在
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("gapAddress", lock.getGapAddress());
        map.put("active", true);
        List<Lock> list = lockMapper.getLocks(map);
        if(list.size() > 0) {
            throw new USException(USException.ErrorCode.ActiveLockWithSameGapAddressExists);
        }


        lock.setActive(true);
        lock.setConstantEncryptWord(LockWordFactory.generateLockWord());
        lock.setDisposableEncryptWord(LockWordFactory.generateLockWord());

        lock.setCreateDate(new Date());
        lock.setUpdateDate(new Date());

        // 找到相应的版本号
        // 这里从console过来的请求直接传cfw.id，从app过来的请求传version的major和minor域
        if(lock.getCurrentFirmwareVersion() != null && lock.getCurrentFirmwareVersion().getId() == null) {
            map.clear();
            map.put("major", lock.getCurrentFirmwareVersion().getMajor());
            map.put("minor", lock.getCurrentFirmwareVersion().getMinor());
            map.put("type", Version.typeLockFirmware);
            List<Version> versions = versionMapper.getVersions(map);
            if (versions.size() == 0) {
                throw new USException(USException.ErrorCode.NoSuchVersion);
            } else {
                lock.setCurrentFirmwareVersion(versions.get(0));
            }
        }

        try {
            lockMapper.addLock(lock);
        }
        catch (DuplicateKeyException e) {
            if(e.getMessage().contains("lock_houseId")) {
                throw new USException(USException.ErrorCode.LockExistsOnHouse);
            }
            else
                throw e;
        }

        // 逻辑：如果增加锁的房屋已经有主人，则自动分配新的Primary钥匙给他
        House house = houseMapper.getHouseById(lock.getHouse().getId());

        if (house.getOwner() != null) {
            Key key;

            Map<String, Object> map1 = new HashMap<String, Object>();
            map1.put("lockId", lock.getId());
            map1.put("ownerId", house.getOwner().getId());
            map1.put("type", Key.typePrimary);
            List<Key> list1 = keyMapper.getKeys(map1);
            if (list1.size() > 0) {
                // 如果原来就有一把钥匙，则enable it
                assert list1.size() == 1;
                Key key1 = list1.get(0);
                assert key1.getStatus() == Key.statusInactive;

                key = new Key();
                key.setId(key1.getId());
                key.setStatus(Key.statusActive);

                Lock lock2 = lockMapper.getLockById(lock.getId());
                key.setAlias(lock2.getHouse().getInaccurateAddress());
                key.setUpdateDate(new Date());

                keyMapper.updateKey(key);
            } else {
                key = new Key();
                key.setCreateDate(new Date());
                key.setUpdateDate(new Date());
                key.setType(Key.typePrimary);
                key.setStatus(Key.statusActive);
                key.setOwner(house.getOwner());
                key.setLock(lock);
                key.setMaxSharedCount(GlobalConstant.maxSharedCountPerKey);

                if (key.getAlias() == null) {
                    Lock lock2 = lockMapper.getLockById(key.getLock().getId());
                    key.setAlias(lock2.getHouse().getInaccurateAddress());
                }

                keyMapper.addKey(key);
            }
        }
        return lock;
    }

    /**
     * 更新Lock对象
     *
     * @param lock
     * @return
     */
    /*@Transactional
    public Lock updateLock(Lock lock) {
        // 逻辑：如果锁上仍然有活跃的钥匙，则不允许更换House，以避免不安全
        if (hasActiveKeysOnLock(lock)) {
            throw new USException(USException.ErrorCode.ActiveKeyExistsOnLock);
        }

        try {
            lock.setUpdateDate(new Date());
            lockMapper.updateLock(lock);
        }
        catch (DuplicateKeyException e) {
            if(e.getMessage().contains("lock_houseId")) {
                throw new USException(USException.ErrorCode.LockExistsOnHouse);
            }
            else
                throw e;
        }

        // 逻辑：要为新房东的主人创建Primary钥匙
        if (lock.getHouse() != null && lock.getHouse().getId() != null) {
            House house = houseMapper.getHouseById(lock.getHouse().getId());
            if (house.getOwner() != null) {
                Key key;

                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("lockId", lock.getId());
                map1.put("ownerId", house.getOwner().getId());
                map1.put("type", Key.typePrimary);
                List<Key> list1 = keyMapper.getKeys(map1);
                if (list1.size() > 0) {
                    // 如果原来就有一把钥匙，则enable it
                    assert list1.size() == 1;
                    Key key1 = list1.get(0);
                    assert key1.getStatus() == Key.statusInactive;

                    key = new Key();
                    key.setId(key1.getId());
                    key.setStatus(Key.statusActive);

                    Lock lock2 = lockMapper.getLockById(lock.getId());
                    key.setAlias(lock2.getHouse().getInaccurateAddress());
                    key.setUpdateDate(new Date());

                    keyMapper.updateKey(key);
                } else {
                    key = new Key();
                    key.setCreateDate(new Date());
                    key.setUpdateDate(new Date());
                    key.setType(Key.typePrimary);
                    key.setStatus(Key.statusActive);
                    key.setOwner(house.getOwner());
                    key.setLock(lock);
                    key.setMaxSharedCount(GlobalConstant.maxSharedCountPerKey);

                    keyMapper.addKey(key);
                }
            }
        }
        return lock;
    }*/

    /**
     * 删除Lock对象
     *
     * @param lock
     */
    public void deleteLock(Lock lock) {
        // 逻辑：如果锁上有过钥匙，则不允许删除锁，以避免不一致性
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lockId", lock.getId());

        List<Key> list = keyMapper.getKeys(map);
        if (list.size() > 0) {
            throw new USException(USException.ErrorCode.KeyExistsOnLock);
        }
        lockMapper.deleteLock(lock);
        return;
    }

    public List<Lock> locateLock(String gapAddress) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gapAddress", gapAddress);

        return lockMapper.getLocks(map);
    }

    public void deactivateLock(Lock lock) {
        // 要使一把锁不再可用，要确保锁上没有活跃钥匙存在
        if(hasAvailableKeysOnLock(lock)) {
            throw new USException(USException.ErrorCode.AvailableKeyExistsOnLock);
        }

        Lock lock1 = new Lock();
        lock1.setId(lock.getId());
        lock1.setActive(false);
        lock1.setUpdateDate(new Date());

        lockMapper.updateLock(lock1);
    }

    private boolean hasAvailableKeysOnLock(Lock lock) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lockId", lock.getId());

        List<Key> list = keyMapper.getAvailableKeys(map);
        if (list.size() > 0) {
            return true;
        }
        else
            return false;
    }



}

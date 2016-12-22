package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Key;
import com.urent.server.domain.KeyActionLog;
import com.urent.server.domain.Lock;
import com.urent.server.domain.User;
import com.urent.server.persistence.AreaMapper;
import com.urent.server.persistence.KeyActionLogMapper;
import com.urent.server.persistence.KeyMapper;
import com.urent.server.persistence.LockMapper;
import com.urent.server.util.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * KeyService
 * Created by Administrator on 2015/8/16
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class KeyService {

    @Autowired
    KeyMapper keyMapper;

    @Autowired
    LockMapper lockMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    KeyActionLogMapper keyActionLogMapper;

    /**
     * 查询Key对象列表
     *
     * @param queryFilters
     * @return
     */
    public List<Key> getKeys(Map<String, Object> queryFilters) {
        return keyMapper.getKeys(queryFilters);
    }

    /**
     * 查询Key数目
     *
     * @param queryFilters
     * @return
     */
    public Long getKeyCount(Map<String, Object> queryFilters) {
        return keyMapper.getKeyCount(queryFilters);
    }

    /**
     * 查询Key对象
     *
     * @param id
     * @return
     */
    public Key getKeyById(Long id) {
        Key key =  keyMapper.getKeyById(id);
        key.getLock().getHouse().getAddress().setArea(areaMapper.getAreaWithAscendants(
                key.getLock().getHouse().getAddress().getArea().getId()
        ));
        return key;
    }

    /**
     * 增加Key对象
     *
     * @param key
     * @return
     */
    @Transactional
    public Key addKey(Key key) {
        if(!key.getType().equals(Key.typePrimary) && !key.getType().equals(Key.typeTemp)) {
            throw new USException(USException.ErrorCode.IllegalRequestParam, "创建的钥匙的类型只能是主钥匙或者临时钥匙");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lockId", key.getLock().getId());
        map.put("ownerId", key.getOwner().getId());


        if(keyMapper.getAvailableKeys(map).size() > 0) {
            throw new USException(USException.ErrorCode.AvailableKeyExistsOnLock, "同一个用户在同一把锁上只能拥有一把钥匙");
        }

        if(key.getType().equals(Key.typePrimary)) {
            // 应用逻辑：如果要创建一把主钥匙，则必须保证该门锁上没有其它的Available钥匙
            map.clear();
            map.put("lockId", key.getLock().getId());

            if(keyMapper.getAvailableKeys(map).size() > 0) {
                throw new USException(USException.ErrorCode.AvailableKeyExistsOnLock, "锁上已经有其它的主钥匙");
            }
        }
        else {
            // 临时钥匙一定要有expiredDate
            if(key.getExpiredDate() == null) {
                throw new USException(USException.ErrorCode.IllegalDataFormat, "临时钥匙必须要设置过期时间");
            }
        }
        key.setCreateDate(new Date());
        key.setUpdateDate(new Date());
        key.setStatus(Key.statusActive);
        /*if(key.getMaxTimes() != null){
            key.setUsedTimes(0);
        }*/
        // 这两个域已经废弃 by xc 20151014
        key.setMaxTimes(null);
        key.setUsedTimes(null);
        if(key.getAlias() == null || key.getAlias().length() == 0) {
            Lock lock = lockMapper.getLockById(key.getLock().getId());
            key.setAlias(lock.getHouse().getInaccurateAddress());
        }
        if (key.getType().equals(Key.typePrimary))
            key.setMaxSharedCount(GlobalConstant.maxSharedCountPerKey);
        else
            key.setMaxSharedCount(0);
        keyMapper.addKey(key);
        return key;
    }

    /**
     * 更新Key对象
     *
     * @param key
     * @return
     */
    public Key updateKey(Key key) {
        key.setUpdateDate(new Date());
        keyMapper.updateKey(key);
        return key;
    }

    /**
     * 删除Key对象
     *
     * @param key
     */
    public void deleteKey(Key key) {
        keyMapper.deleteKey(key);
        return;
    }


    /**使一个钥匙变得不可用
     *
     * @param key
     */
    @Transactional
    public void deactivateKey(Key key, User manager) {
        //逻辑，如果是deactivate一把Primary Key，则需要把它所share的key全部deactivate掉
        Date now = new Date();
        if(key.getType().equals(Key.typePrimary)) {
            if (key.getStatus().equals(Key.statusActive) ||
                    key.getStatus().equals(Key.statusLent)) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("type", Key.typeSlave);
                map.put("sharedFromId", key.getId());

                List<Key> list = keyMapper.getAvailableKeys(map);
                Iterator<Key> iterator = list.iterator();
                while (iterator.hasNext()) {
                    Key key1 = iterator.next();
                    KeyActionLog keyActionLog = new KeyActionLog();
                    keyActionLog.setCreateDate(now);
                    keyActionLog.setHouse(key1.getLock().getHouse());
                    keyActionLog.setKey(key1);
                    keyActionLog.setLock(key1.getLock());
                    keyActionLog.setManager(manager);
                    keyActionLog.setLockGapAddress(key1.getLock().getGapAddress());
                    keyActionLog.setTime(now);
                    keyActionLog.setAction(KeyActionLog.ACTION_DEACTIVATE_CHAIN);
                    keyActionLogMapper.addKeyActionLog(keyActionLog);
                    key1.setStatus(Key.statusInactive);
                    key1.setUpdateDate(new Date());
                    keyMapper.updateKeyStatus(key1);
                }

                key = keyMapper.getKeyById(key.getId());
                KeyActionLog keyActionLog = new KeyActionLog();
                keyActionLog.setCreateDate(now);
                keyActionLog.setHouse(key.getLock().getHouse());
                keyActionLog.setKey(key);
                keyActionLog.setLock(key.getLock());
                keyActionLog.setManager(manager);
                keyActionLog.setLockGapAddress(key.getLock().getGapAddress());
                keyActionLog.setTime(now);
                keyActionLog.setAction(KeyActionLog.ACTION_DEACTIVATE);
                keyActionLogMapper.addKeyActionLog(keyActionLog);
                key.setUpdateDate(new Date());
                key.setStatus(Key.statusInactive);
                keyMapper.updateKeyStatus(key);
            }
            else {
                throw new USException(USException.ErrorCode.KeyStatusUnsatisfied, "只有为可用或借出状态的主钥匙可以被停用");
            }
        }
        else if(key.getType().equals(Key.typeTemp)) {
            if(key.getStatus().equals(Key.statusInUseAndOverTime)) {
                // 对于临时钥匙，暂时只支持 3 -> -3这个状态
                key = keyMapper.getKeyById(key.getId());
                KeyActionLog keyActionLog = new KeyActionLog();
                keyActionLog.setCreateDate(now);
                keyActionLog.setHouse(key.getLock().getHouse());
                keyActionLog.setKey(key);
                keyActionLog.setLock(key.getLock());
                keyActionLog.setManager(manager);
                keyActionLog.setLockGapAddress(key.getLock().getGapAddress());
                keyActionLog.setTime(now);
                keyActionLog.setAction(KeyActionLog.ACTION_DEACTIVATE);
                keyActionLogMapper.addKeyActionLog(keyActionLog);
                key.setUpdateDate(new Date());
                key.setStatus(Key.statusExpiredAndAdminCheck);
                keyMapper.updateKeyStatus(key);
            }
            else {
                throw new USException(USException.ErrorCode.KeyStatusUnsatisfied, "只有为超时状态的临时钥匙可以被停用");
            }
        }
        else {
            throw new USException(USException.ErrorCode.KeyStatusUnsatisfied, "只有主钥匙和临时钥匙可以被停用");
        }
    }

    /**
     * 使一个钥匙变得可用
     * @param key
     */
    public void activateKey(Key key, User manager) {
        if(!key.getType().equals(Key.typePrimary) ||
                !key.getStatus().equals(Key.statusLent)){
            throw new USException(USException.ErrorCode.KeyStatusUnsatisfied, "只有为借出状态的主钥匙可以被启用");
        }
        else {
            Date now = new Date();
            key = keyMapper.getKeyById(key.getId());
            KeyActionLog keyActionLog = new KeyActionLog();
            keyActionLog.setCreateDate(now);
            keyActionLog.setHouse(key.getLock().getHouse());
            keyActionLog.setKey(key);
            keyActionLog.setLock(key.getLock());
            keyActionLog.setManager(manager);
            keyActionLog.setLockGapAddress(key.getLock().getGapAddress());
            keyActionLog.setTime(now);
            keyActionLog.setAction(KeyActionLog.ACTION_ACTIVATE);
            keyActionLogMapper.addKeyActionLog(keyActionLog);
            key.setUpdateDate(now);
            keyMapper.activateKey(key);
        }
    }
}

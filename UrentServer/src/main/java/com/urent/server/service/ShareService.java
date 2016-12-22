package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Key;
import com.urent.server.domain.Lock;
import com.urent.server.domain.User;
import com.urent.server.persistence.KeyMapper;
import com.urent.server.persistence.LockMapper;
import com.urent.server.persistence.MyKeyMapper;
import com.urent.server.persistence.ShareMapper;
import com.urent.server.util.KeyIsExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Dell on 2015/8/31.
 */
@Service
public class ShareService {

    @Autowired
    ShareMapper shareMapper;

    @Autowired
    LockMapper lockMapper;

    @Autowired
    KeyMapper keyMapper;

    @Autowired
    MyKeyMapper myKeyMapper;

    public User getNameByMobile(String mobile,User loginUser) {
        // 暂时先注掉  by xc
        /*if (mobile.equals(loginUser.getMobile().trim()))
            throw new USException(USException.ErrorCode.SharedIdEqualsItself);*/
        User user=shareMapper.getNameByMobile(mobile);
        if (user!=null) {
            if (user.getName()!=null) {
                String str="*"+user.getName().substring(1);
                user.setName(str);
            }
        }
        else
            throw new USException(USException.ErrorCode.NoSuchUser,"无匹配的账户");
        return user;
    }

    public List<Key> getMySharedKeys(Map<String, Object> queryFilters) {

        List<Key> list=shareMapper.getMySharedKeys(queryFilters);
        //remove the expired key from list

        for(Iterator<Key> it=list.iterator();it.hasNext();) {
            Key key=it.next();
            if (KeyIsExpired.checkKeyIsExpired(key)) {
                    keyMapper.updateKeyStatus(key);
                    it.remove();
            }

        }

        return list;

    }

    public Long getMySharedKeyCount(Map<String, Object> queryFilters) {
        return shareMapper.getMySharedKeyCount(queryFilters);
    }

    /**
     *
     * @param key  the key from requestBody
     * @param primaryKey the primaryKey of one lock
     * @return shareKey
     */
    public Key addMySharedKey(Key key,Key primaryKey) {
        //检查slavKey数量没有超上限
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("primaryKeyId",key.getId());
        Long total=shareMapper.getMySharedKeyCount(map);
        if (total.intValue()>=primaryKey.getMaxSharedCount()){
                /*sharedCountExceed*/
            throw new USException(USException.ErrorCode.ExceedSharedMaxCount);
        }

        //检查分享的钥匙有效期没有超过primaryKey的有效期
        if (primaryKey.getExpiredDate()!=null) {
            if (key.getExpiredDate().after(primaryKey.getExpiredDate())) {
                /*expired*/
                throw new USException(USException.ErrorCode.ExceedExpiredDate);
            }
        }
        map.clear();
        map.put("lockId",primaryKey.getLock().getId());
        map.put("ownerId",key.getOwner().getId());
        map.put("status", Key.statusActive);
        int hasAKey=keyMapper.getKeyCount(map).intValue();
        if (hasAKey>0)
            throw new USException(USException.ErrorCode.DuplicateKey, "该用户已拥有该房屋钥匙");
        //add a slaveKey
        Key slaveKey=new Key();
        slaveKey.setLock(primaryKey.getLock());
        slaveKey.setSharedFrom(primaryKey);
        slaveKey.setExpiredDate(key.getExpiredDate());
        slaveKey.setOwner(key.getOwner());
        //slaveKey.setMaxTimes(key.getMaxTimes());
        //slaveKey.setMaxTimes(null); // maxtimes已废弃  by xc 20151014
        Lock lock=lockMapper.getLockById(primaryKey.getLock().getId());
        slaveKey.setAlias(lock.getHouse().getInaccurateAddress());
        slaveKey.setStatus(Key.statusActive);
        slaveKey.setCreateDate(new Date());
        slaveKey.setUpdateDate(new Date());
        // key.setUsedTimes(0);       // 不要设置没用的域 by xc 20151016
        slaveKey.setMaxSharedCount(0);
        slaveKey.setType(Key.typeSlave);
        keyMapper.addKey(slaveKey);
       return slaveKey;
    }

    public Key updateMySharedKey(Key key,Key slaveKey) {
        //检查分享的钥匙有效期没有超过primaryKey的有效期
        Key primaryKey = myKeyMapper.getKeyInfoById(slaveKey.getSharedFrom().getId());
        if (primaryKey.getExpiredDate()!=null) {
            if (key.getExpiredDate().after(primaryKey.getExpiredDate())) {
                /*expired*/
                throw new USException(USException.ErrorCode.ExceedExpiredDate,"您设置的有效期超过了您拥有钥匙的有效期");
            }
        }
        Key key1=new Key();
        key1.setId(key.getId());
        key1.setSharedFrom(slaveKey.getSharedFrom());
        key1.setExpiredDate(key.getExpiredDate());
        key1.setUpdateDate(new Date());
        keyMapper.updateKey(key1);
        return key1;
    }

    public void deleteMySharedKey(Long id) {
        Key key=new Key();
        key.setId(id);
        key.setUpdateDate(new Date());
        key.setStatus(Key.statusInactive);
        keyMapper.updateKeyStatus(key);
    }

}

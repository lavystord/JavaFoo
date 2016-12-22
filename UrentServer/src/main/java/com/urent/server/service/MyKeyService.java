package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.*;
import com.urent.server.persistence.*;
import com.urent.server.util.KeyIsExpired;
import com.urent.server.util.LockWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Lavystord on 2015/8/7.
 */
@Service
public class MyKeyService {
    @Autowired
    MyKeyMapper myKeyMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    KeyMapper keyMapper;

    @Autowired
    LockMapper lockMapper;

    @Autowired
    VersionMapper versionMapper;

    @Autowired
    KeyActionLogMapper keyActionLogMapper;

    @Value("${defaultConstantKeyWordLife}")
    String defaultConstantKeyWordLife;

    public Key getMyKeyById(Long id) {

        Key key= myKeyMapper.getMyKeyById(id);
     //   if (key!=null) {
            key.getLock().getHouse().getAddress().setArea(areaMapper.getAreaWithAscendants(key.getLock().getHouse().getAddress().getArea().getId()));
            if (key.getType().equals(Key.typeTemp)) {
                key.getLock().setConstantKeyWord(null);
                key.getLock().setConstantKeyWordExpiredDate(null);
            }
            return key;
       // }
        //else throw new USException(USException.ErrorCode.KeyIdValueBeyondtheBoundary);
        }


    public List<Key> getMyKeys(Map<String, Object> queryFilters) {
        List<Key> list=myKeyMapper.getMyKeys(queryFilters);
        //remove the expired key from list


        for(Iterator<Key> it=list.iterator();it.hasNext();) {
            Key key=it.next();
            if (key.getType().equals(Key.typeTemp)){
                key.getLock().setConstantKeyWord(null);
                key.getLock().setConstantKeyWordExpiredDate(null);
            }

//            if (key.getExpiredDate()!=null) {
//                //if key is expired
//                Date currentDate = new Date();
//                if (currentDate.after(key.getExpiredDate())) {
//                    key.setUpdateDate(currentDate);
//                    keyMapper.deactivateKey(key);
//                    it.remove();
//                }
//            }
            if (KeyIsExpired.checkKeyIsExpired(key)  ) {
                keyMapper.updateKeyStatus(key);
                if (key.getStatus()==Key.statusInactive || key.getStatus()==Key.statusExpiredButNotUse)
                it.remove();
            }

        }

     return list;
    }

    public  Long getMyKeyCount(Map<String, Object> queryFilters) {

       return myKeyMapper.getMyKeyCount(queryFilters);
    }

    public WordInfo getKeyWord(WordInfo wordInfo,Lock lock,Key key, User user) {
        String keyWord;
        if (key.getStatus()!= Key.statusActive)
            throw new USException(USException.ErrorCode.KeyIsOutdate,"您已使用过该钥匙，");

        KeyActionLog keyActionLog=new KeyActionLog();
         /*写入log*/
        keyActionLog.setKey(key);
        keyActionLog.setLock(key.getLock());
        keyActionLog.setManager(user);
        keyActionLog.setHouse(key.getLock().getHouse());
        keyActionLog.setLockGapAddress(key.getLock().getGapAddress());
        keyActionLog.setTime(new Date());


        if (wordInfo.getDisposableLockWord()==null ) {
            if (wordInfo.getConstantLockWord()==null) {
                /*both of lockWord are null*/
                throw new USException(USException.ErrorCode.DataFieldValidateError,"发送的锁原语为空，请重新发送");
            }
            else {
                /*disposableLockWord is null,constantLockWord is not null,if constantLock is legal,then generate
                 * the relevant constantKeyWord */
                if (key.getType().equals(Key.typeTemp))
                    throw new USException(USException.ErrorCode.DataFieldValidateError, "请使用合理的开门方式");
                 if (wordInfo.getConstantLockWord().length()!=16){
                    throw  new USException(USException.ErrorCode.DataFieldValidateError,"所发送数据长度非法");
                }
                keyWord=LockWordFactory.calculateKeyWord(wordInfo.getConstantLockWord(),lock.getConstantEncryptWord());
                wordInfo.setConstantKeyWord(keyWord);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,Integer.parseInt(defaultConstantKeyWordLife));
                Date expiredDate =cal.getTime();
                Lock lock1=new Lock();
                lock1.setId(lock.getId());
                lock1.setConstantKeyWord(keyWord);
                lock1.setUpdateDate(new Date());
                lock1.setConstantKeyWordExpiredDate(expiredDate);
                lockMapper.updateLock(lock1);


                keyActionLog.setCreateDate(new Date());
                keyActionLog.setAction(KeyActionLog.ACTION_GET_CONSTANT_KEYWORD);
                keyActionLog.setData(keyWord);
                keyActionLogMapper.addKeyActionLog(keyActionLog);

                return wordInfo;
            }

        }// end of disposableLockWord is null
        else {
            if (wordInfo.getConstantLockWord()==null) {
                /*constantLockWord is null,disposableLockWord is not null,if disposableLockWord is leal,then generate
                * the relevant disposableKeyWord*/
                if (wordInfo.getDisposableLockWord().length()!=16){
                    throw  new USException(USException.ErrorCode.DataFieldValidateError,"所发送数据长度非法");
                }
                keyWord= LockWordFactory.calculateKeyWord(wordInfo.getDisposableLockWord(),lock.getDisposableEncryptWord());
                wordInfo.setDisposableKeyWord(keyWord);

                keyActionLog.setCreateDate(new Date());
                keyActionLog.setAction(KeyActionLog.ACTION_GET_DISPOSABLE_KEYWORD);
                keyActionLog.setData(keyWord);
                keyActionLogMapper.addKeyActionLog(keyActionLog);

                return wordInfo;

            }
            else {
                /*both of lockword are not null*/
                throw new USException(USException.ErrorCode.DataFieldValidateError,"请指定的开锁方式，当前存在二义性");
            }


        }//end of disposableLockWord is not null


    }

    private void unlockTheLockOnce(UsedInfo usedInfo, User user) {
       //inputData 有效性检查
        if (usedInfo.getKeyId()==null || usedInfo.getTime() == null)
            throw new USException(USException.ErrorCode.DataFieldValidateError);
        if (usedInfo.getVersion()==null || usedInfo.getPowerDensity()==null)
            throw new USException(USException.ErrorCode.DataFieldValidateError);

        Key key=myKeyMapper.getKeyInfoById(usedInfo.getKeyId());
        if (key==null)
            throw new USException(USException.ErrorCode.KeyIsOutdate,"钥匙已失效");


        //记录锁的版本信息和电池电量信息
        Version currentVersion=new Version(usedInfo.getVersion());
        Map<String,Object> map=new HashMap<>();
        map.put("major",currentVersion.getMajor());
        map.put("minor",currentVersion.getMinor());
        map.put("type", Version.typeLockFirmware);
        List<Version> list=versionMapper.getVersions(map);
        if (list.size() >= 1) {
            currentVersion=list.get(0);
        }
        else
            throw new USException(USException.ErrorCode.NoSuchVersion);
        //多读少写
        if (key.getLock().getPowerDensity().intValue()!=usedInfo.getPowerDensity().intValue() || key.getLock().getCurrentFirmwareVersion().getId().longValue()!=currentVersion.getId().longValue()) {
            Lock lock=new Lock();
            lock.setId(key.getLock().getId());
            lock.setCurrentFirmwareVersion(currentVersion);
            lock.setPowerDensity(usedInfo.getPowerDensity());
            lock.setUpdateDate(new Date());
            lockMapper.updateLock(lock);
        }

        /*需要更新usedTimes*/
//        if (key.getMaxTimes()!=null){
//            int usedTimes;
//            if(key.getUsedTimes()==null)  usedTimes=0;
//            else usedTimes=key.getUsedTimes().intValue();
//            int maxTimes=key.getMaxTimes().intValue();
//            usedTimes++;
//            key.setUsedTimes(usedTimes);
//            key.setUpdateDate(new Date());
//            keyMapper.updateKey(key);
            /*key is expired*/
//            if (usedTimes==maxTimes) {
//                key.setUpdateDate(new Date());
//                keyMapper.deactivateKey(key);
//            }
//        } //end maxTimes!=null

        //记录Key log
            KeyActionLog keyActionLog = new KeyActionLog();
            Date currentDate = new Date();
            switch (key.getStatus()) {
                case Key.statusActive:
                    keyActionLog.setAction(KeyActionLog.ACTION_UNLOCK);
                    if (key.getType().equals(Key.typeTemp)) {
                        key.setStatus(Key.statusInUse);
                        key.setUpdateDate(currentDate);
                        keyMapper.updateKeyStatus(key);
                    }
                    break;
                case Key.statusInUse:
                    if (key.getType().equals(Key.typeTemp)) {
                        keyActionLog.setAction(KeyActionLog.ACTION_LEAVE);
                        key.setStatus(Key.statusInactive);
                        key.setUpdateDate(currentDate);
                        keyMapper.updateKeyStatus(key);
                    }
                    else
                        throw new USException(USException.ErrorCode.ThisKeyCannotDoThisAction);
                    break;
                case Key.statusInUseAndOverTime:
                    if (key.getType().equals(Key.typeTemp)) {
                        keyActionLog.setAction(KeyActionLog.ACTION_LEAVE);
                        keyActionLog.setData("User confirm after the key is expired");
                        key.setStatus(Key.statusExpiredAndUserCheck);
                        key.setUpdateDate(currentDate);
                        keyMapper.updateKeyStatus(key);
                    }
                    else
                        throw new USException(USException.ErrorCode.ThisKeyCannotDoThisAction);
                    break;
            }
            keyActionLog.setKey(key);
            keyActionLog.setLock(key.getLock());
            keyActionLog.setHouse(key.getLock().getHouse());
            keyActionLog.setManager(user);
            keyActionLog.setLockGapAddress(key.getLock().getGapAddress());
            keyActionLog.setTime(usedInfo.getTime());
            keyActionLog.setCreateDate(currentDate);
            keyActionLogMapper.addKeyActionLog(keyActionLog);



    }// end unlockTheLockOnce

    @Transactional
    public void unlockTheLockOnce(List<UsedInfo> usedInfos, User user) {
        for(UsedInfo usedInfo : usedInfos) {
            unlockTheLockOnce(usedInfo, user);
        }
    }


    public void updateMyKey(Key key) {
        Key key1 =new Key();
        key1.setId(key.getId());
        key1.setAlias(key.getAlias());
        key1.setUpdateDate(new Date());
        keyMapper.updateKey(key1);
    }


    public void deleteMyKey(Long id) {
        Key key=new Key();
        key.setId(id);
        key.setUpdateDate(new Date());
        key.setStatus(Key.statusInactive);
        keyMapper.updateKeyStatus(key);
    }



    public Key getKeyInfoById(Long keyId) {
        return myKeyMapper.getKeyInfoById(keyId);
    }

    /*public void leaveHouse(Key key) {
          Key tempKey=myKeyMapper.getKeyInfoById(key.getId());
        if (tempKey.getType().equals(Key.typeTemp) ) {
            Key key1 = new Key();
            //
        }
        else
            throw new USException(USException.ErrorCode.IllegalRequestParam);

    }*/


    public String getDfu(Long id) {
        Version version = versionMapper.getVersionById(id);
        return version.getFirmwareFileId();
    }

    public void reportDfu(DfuInfo dfuInfo,Key oneKey, User user) {
//        if (oneKey.getLock().getId().longValue()!=key.getLock().getId().longValue())
//            throw new USException(USException.ErrorCode.DataFieldValidateError, "请求数据不一致");
        Lock lock=new Lock();
        Date currentDate = new Date();
        lock.setId(oneKey.getLock().getId());
        lock.setCurrentFirmwareVersion(dfuInfo.getCurrentFirmwareVersion());
        lock.setUpdateDate(currentDate);
        lockMapper.updateLock(lock);

        KeyActionLog keyActionLog = new KeyActionLog();
        keyActionLog.setKey(oneKey);
        keyActionLog.setLock(oneKey.getLock());
        keyActionLog.setManager(user);
        keyActionLog.setHouse(oneKey.getLock().getHouse());
        keyActionLog.setLockGapAddress(oneKey.getLock().getGapAddress());
        keyActionLog.setAction(KeyActionLog.ACTION_DFU_COMPLETE);
        keyActionLog.setCreateDate(currentDate);
        keyActionLog.setTime(dfuInfo.getTime());
        keyActionLogMapper.addKeyActionLog(keyActionLog);

    }

}

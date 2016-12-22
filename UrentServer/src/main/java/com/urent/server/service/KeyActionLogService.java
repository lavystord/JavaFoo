package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Key;
import com.urent.server.domain.KeyActionLog;
import com.urent.server.domain.User;
import com.urent.server.persistence.KeyActionLogMapper;
import com.urent.server.persistence.KeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * KeyActionLogService
 * Created by Administrator on 2015/8/25
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class KeyActionLogService {

    @Autowired
    KeyActionLogMapper keyActionLogMapper;


    @Autowired
    KeyMapper keyMapper;

    /**
     * 查询KeyActionLog对象列表
     *
     * @param queryFilters
     * @return
     */
    public List<KeyActionLog> getKeyActionLogs(Map<String, Object> queryFilters) {
        return keyActionLogMapper.getKeyActionLogs(queryFilters);
    }

    /**
     * 查询KeyActionLog数目
     *
     * @param queryFilters
     * @return
     */
    public Long getKeyActionLogCount(Map<String, Object> queryFilters) {
        return keyActionLogMapper.getKeyActionLogCount(queryFilters);
    }

    /**
     * 查询KeyActionLog对象
     *
     * @param id
     * @return
     */
    public KeyActionLog getKeyActionLogById(Long id) {
        return keyActionLogMapper.getKeyActionLogById(id);
    }

    /**
     * 增加KeyActionLog对象
     *
     * @param keyActionLog
     * @return
     */
    public KeyActionLog addKeyActionLog(KeyActionLog keyActionLog) {
        keyActionLogMapper.addKeyActionLog(keyActionLog);
        return keyActionLog;
    }

    private Key getKeyAndCheckLegal(Long keyId, User user) {
        Key key = keyMapper.getKeyById(keyId);
        if(key == null)
            throw new USException(USException.ErrorCode.NonexistentObjectId, "传入的keyId不存在");
        else if(!Key.isKeyValid(key))
            throw new USException(USException.ErrorCode.IllegalRequestParam, "请求了过期钥匙");
        else if(!key.getType().equals(Key.typePrimary)) {
            throw new USException(USException.ErrorCode.IllegalRequestParam, "当前只支持主钥匙的查询记录");
        }
        else if(!key.getOwner().getId().equals(user.getId())) {
            throw new USException(USException.ErrorCode.RequestUnauthorized, "只能查询属于自己的钥匙");
        }

        return key;
    }

    public List<KeyActionLog> getKeyUsedLogs(Long keyId, User user, Map<String, Object> queryFilter) {
        Key key = getKeyAndCheckLegal(keyId, user);

        queryFilter.put("createDate", key.getCreateDate());
        queryFilter.put("keyId", keyId);
        queryFilter.put("lockId", key.getLock().getId());
        queryFilter.put("action", KeyActionLog.ACTION_UNLOCK);

        return keyActionLogMapper.getKeyUsedLogs(queryFilter);
    }
    /**
     * 更新KeyActionLog对象
     *
     * @param keyActionLog
     * @return
    public KeyActionLog updateKeyActionLog(KeyActionLog keyActionLog) {
        keyActionLogMapper.updateKeyActionLog(keyActionLog);
        return keyActionLog;
    }

     */
    /**
     * 删除KeyActionLog对象
     *
     * @param keyActionLog
    public void deleteKeyActionLog(KeyActionLog keyActionLog) {
        keyActionLogMapper.deleteKeyActionLog(keyActionLog);
        return;
    }
     */
}

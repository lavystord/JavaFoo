package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.LockType;
import com.urent.server.persistence.LockTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * LockTypeService
 * Created by Administrator on 2015/8/15
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class LockTypeService {

    @Autowired
    LockTypeMapper lockTypeMapper;

    /**
     * 查询LockType对象列表
     * @param queryFilters
     * @return
     */
    public List<LockType> getLockTypes(Map<String, Object> queryFilters) {
        return lockTypeMapper.getLockTypes(queryFilters);
    }

    /**
     * 查询LockType数目
     * @param queryFilters
     * @return
     */
    public Long getLockTypeCount(Map<String, Object> queryFilters) {
        return lockTypeMapper.getLockTypeCount(queryFilters);
    }

    /**
     * 查询LockType对象
     * @param id
     * @return
     */
    public LockType getLockTypeById(Long id) {
        return lockTypeMapper.getLockTypeById(id);
    }

    /**
     * 增加LockType对象
     * @param lockType
     * @return
     */
    public LockType addLockType(LockType lockType) {
        lockTypeMapper.addLockType(lockType);
        return lockType;
    }

    /**
     * 更新LockType对象
     * @param lockType
     * @return
     */
    public LockType updateLockType(LockType lockType) {
        lockTypeMapper.updateLockType(lockType);
        return lockType;
    }

    /**
     * 删除LockType对象
     * @param lockType
     */
    public void deleteLockType(LockType lockType) {
        if(lockTypeMapper.getLockTypeReferenceCount(lockType) > 0) {
            throw new USException(USException.ErrorCode.DeleteReferentResource, "该锁类型存在有效引用，不允许删除");
        }
        lockTypeMapper.deleteLockType(lockType);
        return;
    }
}

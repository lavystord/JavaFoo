package com.urent.server.persistence;

import com.urent.server.domain.Lock;
import com.urent.server.domain.LockType;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public interface LockTypeMapper {
    public List<LockType> getLockTypes(Map map);

    public Long getLockTypeCount(Map map);

    public LockType getLockTypeById(long id);

    public int addLockType(LockType lockType);

    public void updateLockType(LockType lockType);

    public void deleteLockType(LockType lockType);

    public Long getLockTypeReferenceCount(LockType lockType);
}

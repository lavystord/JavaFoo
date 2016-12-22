package com.urent.server.persistence;

import com.urent.server.domain.Lock;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/16
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface LockMapper {
    public List<Lock> getLocks(Map map);

    public Long getLockCount(Map map);

    public Lock getLockById(long id);

    public int addLock(Lock lock);

    public void updateLock(Lock lock);

    public void deleteLock(Lock lock);
}

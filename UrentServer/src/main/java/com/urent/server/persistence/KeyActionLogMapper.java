package com.urent.server.persistence;

import com.urent.server.domain.KeyActionLog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/25
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface KeyActionLogMapper {
    public List<KeyActionLog> getKeyActionLogs(Map map);

    public Long getKeyActionLogCount(Map map);

    public KeyActionLog getKeyActionLogById(long id);

    public int addKeyActionLog(KeyActionLog keyActionLog);

    public List<KeyActionLog> getKeyUsedLogs(Map map);

    //public void updateKeyActionLog(KeyActionLog keyActionLog);

    //public void deleteKeyActionLog(KeyActionLog keyActionLog);
}

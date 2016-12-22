package com.urent.server.persistence;

import com.urent.server.domain.DataModificationLog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/22
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface DataModificationLogMapper {
    public List<DataModificationLog> getDataModificationLogs(Map map);

    public Long getDataModificationLogCount(Map map);

    public DataModificationLog getDataModificationLogById(long id);

    public int addDataModificationLog(DataModificationLog dataModificationLog);

    public void updateDataModificationLog(DataModificationLog dataModificationLog);

    public void deleteDataModificationLog(DataModificationLog dataModificationLog);
}

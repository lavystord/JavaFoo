package com.urent.server.persistence;

import com.urent.server.domain.Key;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/7.
 */
public interface MyKeyMapper {
    public Key getMyKeyById(long id);

    public List<Key> getMyKeys(Map map);

    public Long getMyKeyCount(Map map);

    public Key getKeyInfoById(long id);

    public List<Key> getPrimaryAndSlaveKeysOfOneUser(Long id);

}

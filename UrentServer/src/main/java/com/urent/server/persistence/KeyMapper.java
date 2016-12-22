package com.urent.server.persistence;

import com.urent.server.domain.House;
import com.urent.server.domain.Key;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/16
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface KeyMapper {
    public List<Key> getKeys(Map map);

    public Long getKeyCount(Map map);

//    public Long getKeyCountOfOneLockAndUser(Map map);

    public Key getKeyById(long id);

    public int addKey(Key key);

    public void updateKey(Key key);

    public void deleteKey(Key key);

   //  public List<Key> getPrimaryKeyOfHouse(House house);

    public List<Key> getAvailableKeys(Map map);

//    public void deactivateKey(Key key);

    public void activateKey(Key key);

    public void tempKeyExpired(Key key);

    public void tempKeyUsed(Key key);

    public void updateKeyStatus(Key key);
}

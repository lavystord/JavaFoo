package com.urent.server.persistence;

import com.urent.server.domain.Key;
import com.urent.server.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/8/31.
 */
public interface ShareMapper {

    public User getNameByMobile(String mobile);

    public List<Key> getMySharedKeys(Map map);

    public Long getMySharedKeyCount(Map map);



}

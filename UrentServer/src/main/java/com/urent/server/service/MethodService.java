package com.urent.server.service;

import com.urent.server.domain.RoleUrl;
import com.urent.server.persistence.RoleUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2015/8/20.
 */
@Service
public class MethodService {

    @Autowired
    RoleUrlMapper roleUrlMapper;

    public List<Map<String, String>> getUnrelatedMethods(Map<String, Object> queryFilters) {
        List<RoleUrl> list = roleUrlMapper.getRoleUrls(queryFilters);
        String[] methods = {"GET", "POST", "PUT", "DELETE"};
        boolean[] flags = {true, true, true, true};

        Iterator<RoleUrl> iterator = list.iterator();
        while (iterator.hasNext()) {
            RoleUrl roleUrl = iterator.next();
            if(roleUrl.getMethod().equals("GET")) {
                flags[0] = false;
            }
            else if(roleUrl.getMethod().equals("POST")) {
                flags[1] = false;
            }
            else if(roleUrl.getMethod().equals("PUT")) {
                flags[2] = false;
            }
            else if(roleUrl.getMethod().equals("DELETE")) {
                flags[3] = false;
            }
        }

        List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
        for(int i = 0; i < 4; i ++) {
            if(flags[i]) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("value", methods[i]);
                list1.add(map);
            }
        }

        return list1;
    }
}

package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Role;
import com.urent.server.domain.RoleUrl;
import com.urent.server.domain.User;
import com.urent.server.persistence.RoleMapper;
import com.urent.server.persistence.RoleMenuMapper;
import com.urent.server.persistence.RoleUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/8/11.
 */
@Service
public class AuthorityCheckService {

    @Autowired
    RoleUrlMapper roleUrlMapper;

    @Autowired
    RoleMapper roleMapper;


    @Value("${appContextUrl}")
    String appContextUrl;

    private static Role anyRole;
    private static Role unlimitedRole;

    static {
        anyRole = new Role();
        anyRole.setId(RoleUrl.ROLE_ANY);
        unlimitedRole = new Role();
        unlimitedRole.setId(RoleUrl.ROLE_UNLIMITED);
    }

    private String normalizeUrl(String url) {
        if(appContextUrl.length() > 0 && url.startsWith(appContextUrl)) {
            url = url.substring(appContextUrl.length());
        }
        Pattern p = Pattern.compile(".*/(\\d)+$");
        Matcher m = p.matcher(url);
        if(m.matches()){
            return url.replaceFirst("/(\\d)+$", "/{id}");
        }
        else {
            return url;
        }
    }

    public boolean checkAuthority(User user, String url, String method) {
        if(url.endsWith(".js") || url.endsWith(".css") || url.endsWith(".html")
                || url.endsWith(".json")|| url.endsWith(".gif")|| url.endsWith(".png")
                || url.endsWith(".ico") || url.endsWith(".icon"))
            return true;
        String normalizedUrl = normalizeUrl(url);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("url", normalizedUrl);
        map.put("method", method);
        if(user == null) {
            map.put("roleId", RoleUrl.ROLE_UNLIMITED);
            if(roleUrlMapper.getRoleUrlCount(map) > 0)
                return true;
        }
        else {
            List<Role> list = roleMapper.getRolesByUser(user);
            list.add(anyRole);
            list.add(unlimitedRole);
            map.put("roles", list);
            if(roleUrlMapper.getRoleUrlCount(map) > 0)
                return true;
        }

        return false;
    }
}

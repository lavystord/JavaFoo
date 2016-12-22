package com.urent.server.persistence;

import com.urent.server.domain.RoleUrl;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/19
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface RoleUrlMapper {
    public List<RoleUrl> getRoleUrls(Map map);

    public Long getRoleUrlCount(Map map);

    public RoleUrl getRoleUrlById(long id);

    public int addRoleUrl(RoleUrl roleUrl);

    public void updateRoleUrl(RoleUrl roleUrl);

    public void deleteRoleUrl(RoleUrl roleUrl);
}

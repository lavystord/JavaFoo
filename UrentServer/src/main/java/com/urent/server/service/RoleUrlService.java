package com.urent.server.service;

import com.urent.server.domain.RoleUrl;
import com.urent.server.persistence.RoleUrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * RoleUrlService
 * Created by Administrator on 2015/8/20
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class RoleUrlService {

    @Autowired
    RoleUrlMapper roleUrlMapper;

    /**
     * 查询RoleUrl对象列表
     *
     * @param queryFilters
     * @return
     */
    public List<RoleUrl> getRoleUrls(Map<String, Object> queryFilters) {
        return roleUrlMapper.getRoleUrls(queryFilters);
    }

    /**
     * 查询RoleUrl数目
     *
     * @param queryFilters
     * @return
     */
    public Long getRoleUrlCount(Map<String, Object> queryFilters) {
        return roleUrlMapper.getRoleUrlCount(queryFilters);
    }

    /**
     * 查询RoleUrl对象
     *
     * @param id
     * @return
     */
    public RoleUrl getRoleUrlById(Long id) {
        return roleUrlMapper.getRoleUrlById(id);
    }

    /**
     * 增加RoleUrl对象
     *
     * @param roleUrl
     * @return
     */
    public RoleUrl addRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.addRoleUrl(roleUrl);
        return roleUrl;
    }

    /**
     * 更新RoleUrl对象
     *
     * @param roleUrl
     * @return
     */
    public RoleUrl updateRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.updateRoleUrl(roleUrl);
        return roleUrl;
    }

    /**
     * 删除RoleUrl对象
     *
     * @param roleUrl
     */
    public void deleteRoleUrl(RoleUrl roleUrl) {
        roleUrlMapper.deleteRoleUrl(roleUrl);
        return;
    }
}

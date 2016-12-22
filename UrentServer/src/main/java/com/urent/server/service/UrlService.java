package com.urent.server.service;

import com.urent.server.domain.Url;
import com.urent.server.persistence.UrlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * UrlService
 * Created by Administrator on 2015/8/19
 * Using template "SpringMvcService" created by Xc
 * For project UrentServer
 * <p/>
 * <p>Langya Technology</p>
 */
@Service
public class UrlService {

    @Autowired
    UrlMapper urlMapper;

    /**
     * 查询Url对象列表
     *
     * @param queryFilters
     * @return
     */
    public List<Url> getUrls(Map<String, Object> queryFilters) {
        return urlMapper.getUrls(queryFilters);
    }

    /**
     * 查询Url数目
     *
     * @param queryFilters
     * @return
     */
    public Long getUrlCount(Map<String, Object> queryFilters) {
        return urlMapper.getUrlCount(queryFilters);
    }

    /**
     * 查询Url对象
     *
     * @param id
     * @return
     */
    public Url getUrlById(Long id) {
        return urlMapper.getUrlById(id);
    }

    /**
     * 增加Url对象
     *
     * @param url
     * @return
     */
    public Url addUrl(Url url) {
        urlMapper.addUrl(url);
        return url;
    }

    /**
     * 更新Url对象
     *
     * @param url
     * @return
     */
    public Url updateUrl(Url url) {
        urlMapper.updateUrl(url);
        return url;
    }

    /**
     * 删除Url对象
     *
     * @param url
     */
    public void deleteUrl(Url url) {
        urlMapper.deleteUrl(url);
        return;
    }
}

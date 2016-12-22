package com.urent.server.persistence;

import com.urent.server.domain.Url;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/19
 * Using template "SpringMvcMapper" created by Xc
 * For project UrentServer
 * <p/>
 * Langya Technology
 */
public interface UrlMapper {
    public List<Url> getUrls(Map map);

    public Long getUrlCount(Map map);

    public Url getUrlById(long id);

    public int addUrl(Url url);

    public void updateUrl(Url url);

    public void deleteUrl(Url url);
}

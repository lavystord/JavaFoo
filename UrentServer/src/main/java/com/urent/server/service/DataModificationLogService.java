package com.urent.server.service;

import com.urent.server.controller.DataModificationFilter;
import com.urent.server.domain.DataModificationLog;
import com.urent.server.domain.User;
import com.urent.server.persistence.DataModificationLogMapper;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/22.
 */
@Service
public class DataModificationLogService {

    @Autowired
    DataModificationLogMapper dataModificationLogMapper;

    static final Logger logger = Logger.getLogger(DataModificationLogService.class);

    public void logDataDataModification(User user, HttpServletRequest request, Object o) {
        if (request.getMethod().equals("GET"))
            return;         // GET暂时不做记录


        if (o.getClass() == HandlerMethod.class) {
            DataModificationLog log = new DataModificationLog();

            HandlerMethod handlerMethod = (HandlerMethod) o;
            Class clazz = handlerMethod.getMethod().getDeclaringClass();
            Method method = handlerMethod.getMethod();
            log.setCalledClassName(clazz.getSimpleName());
            log.setCalledMethodName(method.getName());
            log.setMethod(request.getMethod());
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setRemoteAddress(request.getRemoteAddr());
            log.setUser(user);
            log.setCreateDate(new Date());

            // 只记录json访问，不记录login的具体内容
            if ((request.getContentType() == null || request.getContentType().contains("application/json")) && !request.getRequestURI().equals("/login")) {
                if(request.getClass() == DataModificationFilter.CustomHttpServletRequestWrapper.class) {
                    DataModificationFilter.CustomHttpServletRequestWrapper wrapper = (DataModificationFilter.CustomHttpServletRequestWrapper) request;
                    log.setBodyData(wrapper.getBody());
                }
            }

            dataModificationLogMapper.addDataModificationLog(log);
        }


        return;
    }

    /**
     *
     * @param queryFilters
     * @return
     */
    public List<DataModificationLog> getDataModificationLogs(Map<String, Object> queryFilters) {
        return dataModificationLogMapper.getDataModificationLogs(queryFilters);
    }

    /**
     *
     * @param queryFilter
     * @return
     */
    public Long getDataModificationLogCount(Map<String, Object> queryFilter) {
        return dataModificationLogMapper.getDataModificationLogCount(queryFilter);
    }

    public DataModificationLog getDataModificationLogById(Long id) {
        return dataModificationLogMapper.getDataModificationLogById(id);
    }
}

package com.urent.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.User;
import com.urent.server.service.AuthorityCheckService;
import com.urent.server.service.DataModificationLogService;
import com.urent.server.util.GlobalConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-18
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class AuthorityCheckInterceptor implements HandlerInterceptor {

    static final Logger logger = Logger.getLogger(AuthorityCheckInterceptor.class);
    private static final String success = "success";
    private static final String errorCode = "errorCode";
    private static final String message = "message";

    @Autowired
    AuthorityCheckService authorityCheckService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DataModificationLogService logDataModificationService;

    @Value("${returnDetailExceptionInfo}")
    Boolean returnDeatilExceptionInfo = false;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        User user = (User)request.getSession().getAttribute(GlobalConstant.userConstant);

        if(authorityCheckService.checkAuthority(user, request.getRequestURI(), request.getMethod()) == true) {
            logDataModificationService.logDataDataModification(user, request, o);
            return true;
        }
        else {
            if(user == null) {
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
            else {
                httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.setCharacterEncoding("UTF-8");
                USException e = new USException(USException.ErrorCode.RequestUnauthorized, "当前用户无权访问"+ request.getRequestURI() + "，["
                + request.getMethod() + "]方法");

                logger.error("[Get USException]", e);

                Map<String, Object> map = new HashMap<String, Object>();

                map.put(success, false);
                map.put(errorCode, e.getErrorCode().getCode());
                if(returnDeatilExceptionInfo) {
                    map.put(message, e.getMessage());
                }
                else {
                    map.put(message, e.getErrorCode().getDefaultMessage());
                }
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(map));

                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}

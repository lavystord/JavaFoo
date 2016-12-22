package com.urent.server.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.User;
import com.urent.server.domain.View;
import com.urent.server.service.FileStorageService;
import com.urent.server.service.RegisterService;
import com.urent.server.service.UserService;
import com.urent.server.util.CommonDataFormatTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2015/9/16.
 */
@RestController
public class UserController2 {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Autowired
    RegisterService registerService;

    @Autowired
    FileStorageService fileStorageService;

    @Value("${supportedImageFileTypes}")
    String supportedImageFileType;

    String [] supportedImageFileTypes;


    @RequestMapping(value = "/user2", method = RequestMethod.GET)
    //@JsonView(View.Summary.class)
    public Map<String,Object> getUserList(@RequestParam(value = "start")Integer start, @RequestParam(value = "limit") Integer limit,
                                          @RequestParam(value = "filter", required = false)String filterText,
                                          @RequestParam(value = "sort", required = false) String sortText) {
        Map<String, Object> queryFilter = CommonDataFormatTool.formatQueryFilter(start, limit, filterText, sortText,
                objectMapper);
        List<User> list = userService.getUserList(queryFilter);
        Long total = userService.getUserCount(queryFilter);

        return CommonDataFormatTool.formatListResult(total, list);
    }
}

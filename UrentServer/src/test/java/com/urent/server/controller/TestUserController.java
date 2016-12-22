package com.urent.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.GetRegisterCodeInfo;
import com.urent.server.domain.LoginInfo;
import com.urent.server.domain.RegisterInfo;
import com.urent.server.domain.User;
import com.urent.server.utility.RandomFactory;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
/**
 * Created by Administrator on 2015/7/26.
 */


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserController {
    private MockMvc mockMvc;

    @Autowired
    private UserController userControlleruserController;

    @Autowired
    private GlobalControllerExceptionHandler globalControllerExceptionHandler;

    @Autowired
    ObjectMapper objectMapper;


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Before
    public void setup() {
        // this.mockMvc = MockMvcBuilders.standaloneSetup(userController, globalControllerExceptionHandler).alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();
        /*
        final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();

        //here we need to setup a dummy application context that only registers the GlobalControllerExceptionHandler
        final StaticApplicationContext applicationContext = new StaticApplicationContext();
        applicationContext.registerBeanDefinition("advice", new RootBeanDefinition(GlobalControllerExceptionHandler.class, null, null));

        //set the application context of the resolver to the dummy application context we just created
        exceptionHandlerExceptionResolver.setApplicationContext(applicationContext);

        //needed in order to force the exception resolver to update it's internal caches
        exceptionHandlerExceptionResolver.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(userController).setHandlerExceptionResolvers(exceptionHandlerExceptionResolver)
                    .alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();
                    */

        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();
    }

    private ResultActions logout(HttpSession session) throws Exception {
        ResultActions actions = mockMvc.perform(post("/logout").session((MockHttpSession) session).contentType(MediaType.APPLICATION_JSON));
        return actions;
    }


    private ResultActions login(User user) throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(user.getMobile());
        loginInfo.setPassword(user.getPassword());
        loginInfo.setDevice(RandomFactory.getString(15));

        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));

        return actions;
    }

    private ResultActions register(User user) throws Exception {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setUser(user);
        registerInfo.setRegisterCode(RandomFactory.getNum(10000, 99999));
        registerInfo.setDevice(RandomFactory.getString(16));
        String json = objectMapper.writeValueAsString(registerInfo);

        MockMultipartFile jsonPart = new MockMultipartFile("registerInfo", "", "application/json", json.getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        return mockMvc.perform(MockMvcRequestBuilders.fileUpload("/register").file(jsonPart).contentType(mediaType));
    }

    private ResultActions register(User user, String headerImageFileName) throws Exception {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setUser(user);
        registerInfo.setRegisterCode(RandomFactory.getNum(10000, 99999));
        registerInfo.setDevice(RandomFactory.getString(16));
        String json = objectMapper.writeValueAsString(registerInfo);

        FileInputStream stream = new FileInputStream(headerImageFileName);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", headerImageFileName, "image/png", stream);
        MockMultipartFile jsonPart = new MockMultipartFile("registerInfo", "", "application/json", json.getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        return mockMvc.perform(MockMvcRequestBuilders.fileUpload("/register").file(mockMultipartFile).file(jsonPart).contentType(mediaType));
    }

    private ResultActions getRegisterCode(GetRegisterCodeInfo getRegisterCodeInfo) throws Exception {

        String json = objectMapper.writeValueAsString(getRegisterCodeInfo);
        ResultActions actions = mockMvc.perform(post("/registerCode").content(json).contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"));
        return actions;
    }


    @Test
    public void test0_0register() throws Exception{
        User user = RandomFactory.getUser();
        GetRegisterCodeInfo getRegisterCodeInfo = new GetRegisterCodeInfo();
        getRegisterCodeInfo.setMobile(user.getMobile());
        getRegisterCodeInfo.setCheckUnique(true);

        getRegisterCode(getRegisterCodeInfo).andExpect(status().isOk());

        register(user).andExpect(status().isOk());
    }

    @Test
    public void test0_1register() throws Exception {
        User user = RandomFactory.getUser();
        user.setMobile("1399999999");
        GetRegisterCodeInfo getRegisterCodeInfo = new GetRegisterCodeInfo();
        getRegisterCodeInfo.setMobile(user.getMobile());

        getRegisterCode(getRegisterCodeInfo).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(USException.ErrorCode.DataFieldValidateError.getCode()));
        register(user).andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value(USException.ErrorCode.DataFieldValidateError.getCode()));
    }

    @Test
    public void test0_2register() throws Exception{
        User user = RandomFactory.getUser();
        GetRegisterCodeInfo getRegisterCodeInfo = new GetRegisterCodeInfo();
        getRegisterCodeInfo.setMobile(user.getMobile());
        getRegisterCodeInfo.setCheckUnique(true);

        getRegisterCode(getRegisterCodeInfo).andExpect(status().isOk());

        System.out.println(UserController2.class.getResource("/"));
        register(user, "src/test/resources/mylittledaughter.png").andExpect(status().isOk());
    }


    @Test
    public void test1_0login() throws Exception {
        User user = RandomFactory.getUser();
        register(user).andExpect(status().isOk());

        login(user).andExpect(status().isOk());
    }

    @Test
    public void test1_1login_logout() throws Exception {
        User user = RandomFactory.getUser();
        register(user);

        ResultActions actions = login(user);
        actions.andExpect(status().isOk());

        actions = logout(actions.andReturn().getRequest().getSession());
    }

    @Test
    public void test2_0logout() throws Exception {
    }
}

package com.urent.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.*;
import com.urent.server.domains.GetListResult;
import com.urent.server.utility.DBTestSQL;
import com.urent.server.utility.DataGenerator;
import com.urent.server.utility.RandomFactory;
import org.apache.ibatis.annotations.Select;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Administrator on 2015/11/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
public class TestKeyActionLogController {
    private MockMvc mockMvc;


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private DataGenerator dataGenerator;

    private User newUser;

    private MockHttpSession newUserSession;

    private User loginUser;

    private MockHttpSession loginUserSession;


    public void newUserLogin() throws Exception {
        this.newUser = dataGenerator.generateUser();
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(newUser.getMobile());
        loginInfo.setPassword(newUser.getPassword());
        loginInfo.setDevice("ios: test-session");

        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));

        this.newUserSession = (MockHttpSession) actions.andReturn().getRequest().getSession();

    }

    public void login(User user) throws Exception {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(user.getMobile());
        loginInfo.setPassword(user.getPassword());
        loginInfo.setDevice("ios: test-session");
        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));
        this.loginUser = user;
        this.loginUserSession = (MockHttpSession) actions.andReturn().getRequest().getSession();

    }

    /*
    * 生成一把主钥匙，其中过期时间和原语都为空
    * */
    public void generateOnePrimaryKey() throws Exception {
        dataGenerator.generatePrimaryOrTempKey(null, null, Key.typePrimary);
    }

    public Lock generateOnePrimaryKeyOfSpecificUser(User user) throws Exception {
        Address address = dataGenerator.generateAddress(null);
        House house = dataGenerator.generateHouse(address, user);
        Lock lock = dataGenerator.generateLock(house, null);
        return lock;
    }

    /*
    * 生成若干把主钥匙，作为干扰数据
    * */
    public void generateRandomPrimaryKeys() throws Exception {
        int num = RandomFactory.getNum(3, 6);
        for (int i = 0; i < num; ++i) generateOnePrimaryKey();
    }

    public List<Lock> generateRandomPrimaryKeysOfSpecificUser(User user) throws Exception {
        int num = RandomFactory.getNum(2, 5);
        List<Lock> list = new ArrayList<Lock>();
        for (int i = 0; i < num; i++) list.add(generateOnePrimaryKeyOfSpecificUser(user));
        return list;
    }


    @BeforeClass
    public static void init() throws Exception{
        DBTestSQL.init();
        System.out.println("clear the data");
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();
        dataGenerator = new DataGenerator(mockMvc, objectMapper);
        generateRandomPrimaryKeys();

        newUserLogin();
    }

    private List<KeyActionLog> getKeyUsedLogs(int start, int limit, Key key, MockHttpSession session) throws Exception {

        ResultActions actions = mockMvc.perform(get("/myKey/log").session(session).contentType(MediaType.APPLICATION_JSON)
                .param("start", String.valueOf(start))
                .param("limit", String.valueOf(limit))
                .param("keyId", String.valueOf(key.getId())));
        actions.andExpect(status().isOk());

        String result = actions.andReturn().getResponse().getContentAsString();
        GetListResult<KeyActionLog>  result1 = objectMapper.readValue(result, new TypeReference<GetListResult<KeyActionLog>>() {
        });
        return result1.getList();
    }

    @Test
    public void testGetKeyUsedLog1() throws Exception {
        Lock lock = dataGenerator.generateLock(null, null);

        Key primaryKey = dataGenerator.generatePrimaryOrTempKey(lock, newUser, Key.typePrimary);
        dataGenerator.unlock(primaryKey, newUserSession);

        List<KeyActionLog> list = getKeyUsedLogs(0, 10, primaryKey, newUserSession);

        Assert.assertEquals(list.size(), 1);

        // 加一把分享钥匙
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);


        User user = dataGenerator.generateUser();
        MockHttpSession session = dataGenerator.login(user);
        Key slaveKey = dataGenerator.generateSlaveKey(primaryKey, user, calendar.getTime(), newUserSession);

        dataGenerator.unlock(slaveKey, session);


        list = getKeyUsedLogs(0, 10, primaryKey, newUserSession);
        Assert.assertEquals(list.size(), 2);

        // 重复使用上两把钥匙
        Thread.sleep(1000);
        dataGenerator.unlock(primaryKey, newUserSession);

        Thread.sleep(1000);
        dataGenerator.unlock(slaveKey, session);

        list = getKeyUsedLogs(0, 10, primaryKey, newUserSession);
        Assert.assertEquals(list.size(), 4);

        // 产生一把临时钥匙
        User user2 = dataGenerator.generateUser();
        MockHttpSession session2 = dataGenerator.login(user2);

        Key tempKey = dataGenerator.generatePrimaryOrTempKey(lock, user2, "temp");
        dataGenerator.unlock(tempKey, session2);

        list = getKeyUsedLogs(0, 10, primaryKey, newUserSession);
        Assert.assertEquals(list.size(), 5);

        dataGenerator.tempKeyLeave(tempKey, session2);

        // 原主钥匙使失效
        dataGenerator.deactivateKey(primaryKey);

        // 创建新的主钥匙
        primaryKey = dataGenerator.generatePrimaryOrTempKey(lock, newUser, Key.typePrimary);

        list = getKeyUsedLogs(0, 10, primaryKey, newUserSession);
        Assert.assertEquals(list.size(), 0);slaveKey = dataGenerator.generateSlaveKey(primaryKey, user, calendar.getTime(), newUserSession);

        dataGenerator.unlock(slaveKey, session);


        list = getKeyUsedLogs(0, 10, primaryKey, newUserSession);
        Assert.assertEquals(list.size(), 1);
    }




}

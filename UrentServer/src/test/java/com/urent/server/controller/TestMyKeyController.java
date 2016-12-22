package com.urent.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.*;
import com.urent.server.domains.GetListResult;
import com.urent.server.util.GlobalConstant;
import com.urent.server.utility.DBTestSQL;
import com.urent.server.utility.DataGenerator;
import com.urent.server.utility.RandomFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Dell on 2015/8/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMyKeyController {

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
        loginInfo.setDevice("Android: test-session");

        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));

        this.newUserSession = (MockHttpSession) actions.andReturn().getRequest().getSession();

    }

    public void login(User user) throws Exception {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(user.getMobile());
        loginInfo.setPassword(user.getPassword());
        loginInfo.setDevice("Android: test-session");
        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));
        this.loginUser = user;
        this.loginUserSession = (MockHttpSession) actions.andReturn().getRequest().getSession();

    }

    /*
    * 生成一把主钥匙，其中过期时间和原语都为空
    * */
    public void generateOnePrimaryKey() throws Exception {
        User user = dataGenerator.generateUser();
        Address address = dataGenerator.generateAddress(null);
        House house = dataGenerator.generateHouse(address, user);
        Lock lock = dataGenerator.generateLock(house, null);
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

    public void generateOneSlaveKey() throws Exception {
        loginUser = dataGenerator.getOneUser();
        login(loginUser);
        Key onekey = dataGenerator.getOnePrimaryKeyfromOneUser(loginUser);
        if (onekey == null) return;
        Long keyId = onekey.getId();
        Key key = new Key();
        key.setId(keyId);
        User user = dataGenerator.generateUser();
        if (user.getId().longValue() != loginUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);
            ResultActions actions = mockMvc.perform(post("/share").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk());
        }
    }

    public Key generateOneSlaveKeyOfSpecificUser(User oneuser) throws Exception {
        login(oneuser);
        Key onekey = dataGenerator.getOnePrimaryKeyfromOneUser(oneuser);
        Long keyId = onekey.getId();
        Key key = new Key();
        key.setId(keyId);
        User user = dataGenerator.generateUser();
        if (user.getId().longValue() != oneuser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
        }
        String jsonString = objectMapper.writeValueAsString(key);
        ResultActions actions = mockMvc.perform(post("/share").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk());
        Key slavekey = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Key.class);
        return slavekey;
    }

    @BeforeClass
    public static void init() throws Exception{
        DBTestSQL.init();
        System.out.println("clear the original data and insert the test data");
    }


    @AfterClass
    public static void restore() throws Exception {
        DBTestSQL.restore();
        System.out.println("restore the data");
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();
        dataGenerator = new DataGenerator(mockMvc, objectMapper);
        generateRandomPrimaryKeys();
        generateOneSlaveKey();
        newUserLogin();
    }

    /*测试用例*/
    @Test
    public void test_MyKeys0() throws Exception {
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(0)));
    }


    /*
     *产生随机数量的key，其中有num把属于当前用户的primary key，返回属于当前用户的钥匙列表.
     * Attention:the generated key's expiredDate and constantKeyWord is null
     *
     */
    @Test
    public void test_MyKeys1() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        Long total = new Long(list.size());
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));

        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        Assert.assertEquals(list.get(total2 - 1).getId(), listResult.getList().get(0).getLock().getId());
        Assert.assertEquals(listResult.getTotal(), total);

    }


    /*
     * todo 暂时未涉及有关slave key存在的getMyKeys的测试，注意slave key可能存在超过过期时间的情况
     */

    /*随机用户登录，只有primary key时随机获取一把属于他的钥匙获取keyWord,disposable原语*/
    @Test
    public void test_keyWord0() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(listResult.getList().get(num).getId());
        wordInfo.setDisposableLockWord("qwe344tyu!9e241g");
        wordInfo.setConstantLockWord(null);
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk()).andExpect(jsonPath("$constantKeyWord").doesNotExist());

    }

    /*随机用户登录，只有primary key时随机获取一把属于他的钥匙获取keyWord,constant原语*/
    @Test
    public void test_keyWord1() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        String lockword = "!@#$%^&*()123AbC";
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(listResult.getList().get(num).getId());
        wordInfo.setConstantLockWord(lockword);
        wordInfo.setDisposableLockWord(null);
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk()).andExpect(jsonPath("$disposableKeyWord").doesNotExist());
    }

    /*所请求钥匙不属于当前登录用户,errorcode为RequestUnauthorized，8*/
    @Test
    public void test_keyWord2() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
//        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
//        });
//        int num=RandomFactory.getNum(0,total2);
//        int keyId=listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(1L);
        wordInfo.setConstantLockWord("1234567890aderwq");
        wordInfo.setDisposableLockWord(null);
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.RequestUnauthorized.getCode())));
    }


    /*发送锁原语为null，errorCode为DataFieldValidateError,7*/
    @Test
    public void test_keyWord3() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(listResult.getList().get(num).getId());
        wordInfo.setConstantLockWord(null);
        wordInfo.setDisposableLockWord(null);
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DataFieldValidateError.getCode())));
    }

    /*发送现不存在的keyId,errorCode为KeyIsOutdate，102*/
    @Test
    public void test_keyWord4() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(new Long(6553685));
        wordInfo.setConstantLockWord("4789kiuyliki6n!@");
        wordInfo.setDisposableLockWord("%dseiewa09231369");
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));
    }

    /*发送锁原语，两者都不为空，errorCode为DataFieldValidateError，7*/
    @Test
    public void test_keyWord5() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(listResult.getList().get(num).getId());
        wordInfo.setConstantLockWord("4789kiuyliki6n!@");
        wordInfo.setDisposableLockWord("%dseiewa09231369");
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DataFieldValidateError.getCode())));
    }

    /*发送锁原语，keyId为空，valid验证不通过，errorCode为DataFieldValidateError，7*/
    @Test
    public void test_keyWord6() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(null);
        wordInfo.setConstantLockWord("4789kiuyliki6n!@");
        wordInfo.setDisposableLockWord("%dseiewa09231369");
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DataFieldValidateError.getCode())));
    }

    /*发送锁原语长度不为16,errorCode为DataFieldValidateError，7*/
    @Test
    public void test_keyWord7() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(listResult.getList().get(num).getId());
        wordInfo.setConstantLockWord(null);
        wordInfo.setDisposableLockWord("%dseiewa0926631369");
        String jsonString = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DataFieldValidateError.getCode())));
    }

    /*正常情况，keyId属于数据库*/
    @Test
    public void test_keyUsed0() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
//        int keyId=listResult.getList().get(num).getId().intValue();
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(listResult.getList().get(num).getId());
        usedInfo.setTime(new Date());
        usedInfo.setVersion("1.1");
        usedInfo.setPowerDensity(88);
        List<UsedInfo> usedInfos = new ArrayList<UsedInfo>(1);
        usedInfos.add(usedInfo);
        String jsonString = objectMapper.writeValueAsString(usedInfos);
        actions = mockMvc.perform(post("/myKey/keyUsed").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

    }

    /*keyId越界,errorcode为KeyIdValueBeyondtheBoundary，115*/
    @Test
    public void test_keyUsed1() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
//        int num=RandomFactory.getNum(0,total2);
//        int keyId=listResult.getList().get(num).getId().intValue();
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(new Long(655368596));
        usedInfo.setTime(new Date());
        usedInfo.setVersion("25.22");
        usedInfo.setPowerDensity(88);
        List<UsedInfo> usedInfos = new ArrayList<UsedInfo>(1);
        usedInfos.add(usedInfo);
        String jsonString = objectMapper.writeValueAsString(usedInfos);
        actions = mockMvc.perform(post("/myKey/keyUsed").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));

    }

    /*keyId为空，valid验证不通过，errorCode为DataFieldValidateError，7*/
    @Test
    public void test_keyUsed2() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
//        int num=RandomFactory.getNum(0,total2);
//        int keyId=listResult.getList().get(num).getId().intValue();
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(null);
        usedInfo.setTime(new Date());
        usedInfo.setVersion("25.22");
        usedInfo.setPowerDensity(88);
        List<UsedInfo> usedInfos = new ArrayList<UsedInfo>(1);
        usedInfos.add(usedInfo);
        String jsonString = objectMapper.writeValueAsString(usedInfos);
        actions = mockMvc.perform(post("/myKey/keyUsed").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DataFieldValidateError.getCode())));
    }

    /*in normal condition*/
    @Test
    public  void test_queryUsername0() throws Exception{
        //User user=newUser;
        ResultActions actions=mockMvc.perform(get("/share/queryName").session(newUserSession).contentType(MediaType.APPLICATION_JSON).param("mobile","13000000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(1)));
    }

    /*share key to himself,errorCode:SharedIdEqualsItself,120*/
   //暂时封掉这种异常情况
   /* @Test
    public  void test_queryUsername1() throws Exception{
        //User user=newUser;
        ResultActions actions=mockMvc.perform(get("/share/queryName").session(newUserSession).contentType(MediaType.APPLICATION_JSON).param("mobile",newUser.getMobile()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode",is(USException.ErrorCode.SharedIdEqualsItself.getCode())));
    }*/

    /*don't exists the queried account,errorCode:UserNotExist,121*/
    @Test
    public  void test_queryUsername2() throws Exception{
        String mobile=RandomFactory.generateMobile();
        ResultActions actions=mockMvc.perform(get("/share/queryName").session(newUserSession).contentType(MediaType.APPLICATION_JSON).param("mobile",mobile))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode",is(USException.ErrorCode.NoSuchUser.getCode())));
    }

    /*parameter Exception*/
    @Test
    public  void test_queryUsername3() throws Exception{
//        String mobile=RandomFactory.generateMobile();
        ResultActions actions=mockMvc.perform(get("/share/queryName").session(newUserSession).contentType(MediaType.APPLICATION_JSON).param("mobile","13579"))
                .andExpect(status().isInternalServerError());
    }


    /*in normal condition*/
    @Test
    public void test_getMySharedKeys0() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
        int total=RandomFactory.getNum(1,GlobalConstant.maxSharedCountPerKey);
        for (int i = 0; i < total; i++) {
            User user = dataGenerator.generateUser();
            if (user.getId().longValue() != newUser.getId().longValue()) {
                key.setOwner(user);
                Date randomDate = RandomFactory.randomDate(new Date());
                key.setExpiredDate(randomDate);
                int maxTimes = RandomFactory.getNum(1, 100);
                key.setMaxTimes(maxTimes);
                String jsonString = objectMapper.writeValueAsString(key);

                actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                        .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));

            }
        }
        actions=mockMvc.perform(get("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).param("id",keyId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total",is(total)));
    }

    /*更改钥匙别名，正常情况*/
    @Test
    public void test_updateKey0() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        String secondName = RandomFactory.getString(15);
        int keyId = listResult.getList().get(num).getId().intValue();
        Key key = new Key();
        key.setId(listResult.getList().get(num).getId());
        key.setAlias(secondName);
        String jsonString = objectMapper.writeValueAsString(key);
        actions = mockMvc.perform(put("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$alias", is(secondName)));

//        actions = mockMvc.perform(get("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$alias", is(secondName)));
    }

    /*keyid越界,由于sql update不会返回错误信息，故返回200*/
    @Test
    public void test_updateKey1() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
//        int num=RandomFactory.getNum(0,total2);
        String secondName = RandomFactory.getString(15);
        Long id = new Long(54646464);
        int keyId = id.intValue();
        Key key = new Key();
        key.setId(id);
        key.setAlias(secondName);
        String jsonString = objectMapper.writeValueAsString(key);
        actions = mockMvc.perform(put("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));

//        actions=mockMvc.perform(get("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$alias",is(secondName)));
    }

    /*urlid  或requestbody中keyId存在null*/
    @Test
    public void test_updateKey2() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
//        int num=RandomFactory.getNum(0,total2);
        String secondName = RandomFactory.getString(15);
        Long id = new Long(54646464);
        int keyId = id.intValue();
        Key key = new Key();
        key.setId(null);
        key.setAlias(secondName);
        String jsonString = objectMapper.writeValueAsString(key);
        actions = mockMvc.perform(put("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DataFieldValidateError.getCode())));

//        actions=mockMvc.perform(get("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$alias",is(secondName)));
    }


    /*删除主钥匙,errorCode为PrimaryKeyCannotBeDeleted，119*/
    @Test
    public void test_deleteKey0() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        int num = RandomFactory.getNum(0, total2);
        int keyId = listResult.getList().get(num).getId().intValue();

        actions = mockMvc.perform(delete("/myKey/{id}", keyId).session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.ThisKeyCannotDoThisAction.getCode())));

    }

    /*in normal conditon,share to lockkeeper*/
    @Test
    public void test_deleteKey1() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
        User user = new User();
        user.setId(2L);
        user.setPassword(DataGenerator.lockKeeperPassword);
        user.setMobile(DataGenerator.lockKeeperMobile);
        key.setOwner(user);
        Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);
            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk());
        Key slaveKey=objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        login(user);

        actions = mockMvc.perform(delete("/myKey/{id}", slaveKey.getId().intValue()).session(loginUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

    }
    /*errorCode:RequestUnauthorized*/
    @Test
    public void test_deleteKey2() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        ResultActions actions = mockMvc.perform(delete("/myKey/{id}", 5).session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.RequestUnauthorized.getCode())));

    }

    /*正常情况，增加一把slave key*/
       @Test
    public void test_addSharedKey0() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
//            int maxTimes = RandomFactory.getNum(1, 100);
//            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                    .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));



        }

    }

    /*请求钥匙不属于当前登录用户，errocode为RequestUnauthorized，8*/
    @Test
    public void test_addSharedKey1() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

//        int num=RandomFactory.getNum(0, total2);
//        Long keyId=listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(1L);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.RequestUnauthorized.getCode())));

        }

    }

    /*keyid越界，errorcode为KeyIdValueBeyondtheBoundary，115*/
    @Test
    public void test_addSharedKey2() throws  Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        Key key = new Key();
        key.setId(65536987L);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));

        }

    }

    /*todo slavekey can't be shared,errorcode为KeyCanntShare，122*/

    /*exceed maxcount,errorcode为ExceedSharedMaxCount，103*/
    @Test
    public void  test_addSharedKey3() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
        for (int i = 0; i < GlobalConstant.maxSharedCountPerKey; i++) {
            User user = dataGenerator.generateUser();
            if (user.getId().longValue() != newUser.getId().longValue()) {
                key.setOwner(user);
                Date randomDate = RandomFactory.randomDate(new Date());
                key.setExpiredDate(randomDate);
                int maxTimes = RandomFactory.getNum(1, 100);
                key.setMaxTimes(maxTimes);
                String jsonString = objectMapper.writeValueAsString(key);

                actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                        .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));

            }
        }// end of for

        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isInternalServerError())
                   .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.ExceedSharedMaxCount.getCode())));

        }
    }

    /*todo (现有primaryKey没有过期时间)slaveKey有效期超出现有钥匙，errorcode为ExceedExpiredDate，123*/
   /* @Test
    public void test_addSharedKey4() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date date=new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR,1);
            Date expiredDate=cal.getTime();
            key.setExpiredDate(expiredDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.ExceedExpiredDate.getCode())));

        }

    }*/

    /*share one key to myself,errorCode为SharedIdequalsitself，120*/
    @Test
    public void test_addSharedKey5() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
       // User user = dataGenerator.getOneUser();
        //if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(newUser);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.SharedIdEqualsItself.getCode())));

        //}

    }

    @Test
    public void test_addSharedKey6() throws Exception {
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        key.setId(keyId);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
//            int maxTimes = RandomFactory.getNum(1, 100);
//            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                    .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));

            randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.DuplicateKey.getCode())));

        }

    }

    /*in normal condition*/
    @Test
    public void  test_updateSharedKey0() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        Key newkey=new Key();
        key.setId(keyId);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                    .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));

            newkey=objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        }
        Date date = RandomFactory.randomDate(new Date());
     //   newkey.setSharedFrom(key);
        newkey.setExpiredDate(date);
//        int maxTimes=RandomFactory.getNum(1,100);
//        newkey.setMaxTimes(maxTimes);
        String jsonString=objectMapper.writeValueAsString(newkey);
        actions=mockMvc.perform(put("/share/{id}",newkey.getId().intValue()).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())));
//                .andExpect(jsonPath("$.maxTimes",is(maxTimes)));
    }

    /*todo (现有primaryKey没有过期时间)slaveKey有效期超出现有钥匙，errorcode为ExceedExpiredDate，123*/
   /* @Test
    public void test_updateSharedKey1() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        Key newkey=new Key();
        key.setId(keyId);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                    .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));

            newkey=objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        }
        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR,1);
        Date expiredDate=cal.getTime();
        //   newkey.setSharedFrom(key);
        newkey.setExpiredDate(expiredDate);
        int maxTimes=RandomFactory.getNum(1,100);
        newkey.setMaxTimes(maxTimes);
        String jsonString=objectMapper.writeValueAsString(newkey);
        actions=mockMvc.perform(put("/share/{id}", newkey.getId().intValue()).session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is(USException.ErrorCode.ExceedExpiredDate.getCode())));
    }*/

    /*in normal condition*/
    @Test
    public void test_deleteSharedKey0() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        Key key = new Key();
        Key slavekey=new Key();
        key.setId(keyId);
        User user = dataGenerator.getOneUser();
        if (user.getId().longValue() != newUser.getId().longValue()) {
            key.setOwner(user);
            Date randomDate = RandomFactory.randomDate(new Date());
            key.setExpiredDate(randomDate);
            int maxTimes = RandomFactory.getNum(1, 100);
            key.setMaxTimes(maxTimes);
            String jsonString = objectMapper.writeValueAsString(key);

            actions = mockMvc.perform(post("/share").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sharedFrom.id", is(keyId.intValue())))
                    .andExpect(jsonPath("$.owner.id", is(user.getId().intValue())));

            slavekey=objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        }
         actions=mockMvc.perform(delete("/share/{id}",slavekey.getId().intValue()).session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.success",is(true)));
    }

    /*errorcode:ThisKeyCannotDoThisAction,119*/
    @Test
    public void test_deleteSharedKey1() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);

       ResultActions  actions=mockMvc.perform(delete("/share/{id}",1).session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isInternalServerError())
                 .andExpect(jsonPath("$.errorCode",is(USException.ErrorCode.ThisKeyCannotDoThisAction.getCode())));
    }

    /* 测试用例已写死 delete the key that don't belongs to himself,errorCode为RequestUnauthorized,8*/
    @Test
    public void test_deleteSharedKey2() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);

       ResultActions  actions=mockMvc.perform(delete("/share/{id}",5).session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isInternalServerError())
                 .andExpect(jsonPath("$.errorCode",is(USException.ErrorCode.RequestUnauthorized.getCode())));
    }


    @Test
    public void test_verifyRealName0()throws Exception {
        User user=(User)newUserSession.getAttribute(GlobalConstant.userConstant);
        User info=new User();
        info.setId(user.getId());
        info.setName("周杰伦");
        info.setIdCardNumber(RandomFactory.generateIdCardNumber());
        String jsonString=objectMapper.writeValueAsString(info);
        ResultActions actions=mockMvc.perform(post("/certify").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success",is(true)));
    }

    @Test
    public void test_dfu0() throws Exception{
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Long keyId = listResult.getList().get(num).getId();
        DfuInfo dfuInfo = new DfuInfo();
        dfuInfo.setKeyId(keyId);
        Version version = new Version();
        version.setId(1L);
        dfuInfo.setCurrentFirmwareVersion(version);
        dfuInfo.setTime(new Date());
        String jsonString=objectMapper.writeValueAsString(dfuInfo);
         actions=mockMvc.perform(post("/myKey/dfu").session(newUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

    }
    /* in normal condition  */
    @Test
    public void test_EverythingAboutTempKey0 () throws Exception {
        //choose a PrimaryKey from newUser
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Key PrimaryKey=listResult.getList().get(num);

        //create a tempKey
        Key tempKey = new Key();
        tempKey.setStatus(Key.statusActive);
        tempKey.setLock(PrimaryKey.getLock());
        Date date=new Date();
        tempKey.setCreateDate(date);
        tempKey.setUpdateDate(date);
        tempKey.setExpiredDate(RandomFactory.randomDate(new Date()));
        User oneUser = dataGenerator.generateUser();
        tempKey.setOwner(oneUser);
        login(oneUser);
        tempKey.setType(Key.typeTemp);

        String jsonOfAddKey=objectMapper.writeValueAsString(tempKey);
        actions=mockMvc.perform(post("/key").session(dataGenerator.getAdminSession()).contentType(MediaType.APPLICATION_JSON).content(jsonOfAddKey))
                    .andExpect(status().isOk());
        Key newTempKey = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Key.class);
        Long tempKeyId=newTempKey.getId();
        //get Key Word
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(tempKeyId);
        wordInfo.setDisposableLockWord("q@e344tyu!9e241g");
        wordInfo.setConstantLockWord(null);
        String jsonOfWordInfo = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfWordInfo))
                .andExpect(status().isOk()).andExpect(jsonPath("$constantKeyWord").doesNotExist());

        //Unlock the door,key is used
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(tempKeyId);
        usedInfo.setTime(new Date());
        usedInfo.setVersion("1.1");
        usedInfo.setPowerDensity(88);
        List<UsedInfo> usedInfos = new ArrayList<UsedInfo>(1);
        usedInfos.add(usedInfo);
        String jsonOfUsedInfo = objectMapper.writeValueAsString(usedInfos);
        actions = mockMvc.perform(post("/myKey/keyUsed").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfUsedInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        //cann't getKeyWord
        actions = mockMvc.perform(post("/myKey/keyWord").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfWordInfo))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));

        //check key's status is 2
        actions = mockMvc.perform(get("/myKey").session(loginUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1));
        GetListResult<Key> listResult2 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        Key tempKey2 = listResult2.getList().get(0);
        assertThat(tempKey2.getStatus().intValue(),is(Key.statusInUse));

        //confirm the user leaved house
        actions = mockMvc.perform(post("/myKey/keyUsed").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfUsedInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        //check key's status is 0
        actions = mockMvc.perform(get("/key/{id}", tempKeyId).session(dataGenerator.getAdminSession()))
                .andExpect(status().isOk());
        Key tempKey3 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        assertThat(tempKey3.getStatus().intValue(),is(Key.statusInactive));

    }

    /*用户没有及时确认离开  后来又重新确认离开*/
    @Test
    public void test_EverythingAboutTempKey1 () throws Exception {
        //choose a PrimaryKey from newUser
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Key PrimaryKey=listResult.getList().get(num);

        //create a tempKey
        Key tempKey = new Key();
        tempKey.setStatus(Key.statusActive);
        tempKey.setLock(PrimaryKey.getLock());
        Date date = new Date();
        tempKey.setCreateDate(date);
        tempKey.setUpdateDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,5);
        Date end=cal.getTime();
        tempKey.setExpiredDate(end);
        long start=System.currentTimeMillis();
        User oneUser = dataGenerator.generateUser();
        tempKey.setOwner(oneUser);
        login(oneUser);
        tempKey.setType(Key.typeTemp);

        String jsonOfAddKey=objectMapper.writeValueAsString(tempKey);
        actions=mockMvc.perform(post("/key").session(dataGenerator.getAdminSession()).contentType(MediaType.APPLICATION_JSON).content(jsonOfAddKey))
                    .andExpect(status().isOk());
        Key newTempKey = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Key.class);
        Long tempKeyId=newTempKey.getId();
        //get Key Word
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(tempKeyId);
        wordInfo.setDisposableLockWord("q@e344tyu!9e241g");
        wordInfo.setConstantLockWord(null);
        String jsonOfWordInfo = objectMapper.writeValueAsString(wordInfo);
        actions = mockMvc.perform(post("/myKey/keyWord").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfWordInfo))
                .andExpect(status().isOk()).andExpect(jsonPath("$constantKeyWord").doesNotExist());

        //Unlock the door,key is used
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(tempKeyId);
        usedInfo.setTime(new Date());
        usedInfo.setVersion("1.1");
        usedInfo.setPowerDensity(88);
        List<UsedInfo> usedInfos = new ArrayList<UsedInfo>(1);
        usedInfos.add(usedInfo);
        String jsonOfUsedInfo = objectMapper.writeValueAsString(usedInfos);
        actions = mockMvc.perform(post("/myKey/keyUsed").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfUsedInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        //cann't getKeyWord
        actions = mockMvc.perform(post("/myKey/keyWord").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfWordInfo))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));

        //check key's status is 2
        actions = mockMvc.perform(get("/myKey").session(loginUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1));
        GetListResult<Key> listResult2 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        Key tempKey2 = listResult2.getList().get(0);
        long end2=System.currentTimeMillis();
        long ans=end2-start;
        System.out.println("----"+ans);
        assertThat(tempKey2.getStatus().intValue(),is(Key.statusInUse));

        Thread.sleep(5000);

        //check key's status is 3
        actions = mockMvc.perform(get("/myKey").session(loginUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1));
        GetListResult<Key> listResult3 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        Key tempKey3 = listResult3.getList().get(0);
        assertThat(tempKey3.getStatus().intValue(),is(Key.statusInUseAndOverTime));

        //confirm the user leaved house again
        actions = mockMvc.perform(post("/myKey/keyUsed").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfUsedInfo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        //check key's status is 0
        actions = mockMvc.perform(get("/key/{id}", tempKeyId).session(dataGenerator.getAdminSession()))
                .andExpect(status().isOk());
        Key tempKey4 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        assertThat(tempKey4.getStatus().intValue(),is(Key.statusExpiredAndUserCheck));

    }

    /*未使用就过期*/
    @Test
    public void test_EverythingAboutTempKey2 () throws Exception {
        //choose a PrimaryKey from newUser
        List<Lock> list = generateRandomPrimaryKeysOfSpecificUser(newUser);
        int total2 = list.size();
        ResultActions actions = mockMvc.perform(get("/myKey").session(newUserSession).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(total2));
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });
        int num = RandomFactory.getNum(0, total2);
        Key PrimaryKey=listResult.getList().get(num);

        //create a tempKey
        Key tempKey = new Key();
        tempKey.setStatus(Key.statusActive);
        tempKey.setLock(PrimaryKey.getLock());
        Date date = new Date();
        tempKey.setCreateDate(date);
        tempKey.setUpdateDate(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND,2);
        Date end=cal.getTime();
        tempKey.setExpiredDate(end);
//        long start=System.currentTimeMillis();
        User oneUser = dataGenerator.generateUser();
        tempKey.setOwner(oneUser);
        login(oneUser);
        tempKey.setType(Key.typeTemp);

        String jsonOfAddKey=objectMapper.writeValueAsString(tempKey);
        actions=mockMvc.perform(post("/key").session(dataGenerator.getAdminSession()).contentType(MediaType.APPLICATION_JSON).content(jsonOfAddKey))
                .andExpect(status().isOk());
        Key newTempKey = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Key.class);

        Long tempKeyId=newTempKey.getId();

        //cann't getKeyWord
        WordInfo wordInfo = new WordInfo();
        wordInfo.setKeyId(tempKeyId);
        wordInfo.setDisposableLockWord("q@e344tyu!9e241g");
        wordInfo.setConstantLockWord(null);
        String jsonOfWordInfo = objectMapper.writeValueAsString(wordInfo);
        //not use but expired
        Thread.sleep(2000);

        actions = mockMvc.perform(post("/myKey/keyWord").session(loginUserSession).contentType(MediaType.APPLICATION_JSON).content(jsonOfWordInfo))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("errorCode", is(USException.ErrorCode.KeyIsOutdate.getCode())));


        //check key's status is -1
        actions = mockMvc.perform(get("/key/{id}", tempKeyId).session(dataGenerator.getAdminSession()))
                .andExpect(status().isOk());
        Key tempKey4 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),Key.class);
        assertThat(tempKey4.getStatus().intValue(),is(Key.statusExpiredButNotUse));

    }





}//end of class


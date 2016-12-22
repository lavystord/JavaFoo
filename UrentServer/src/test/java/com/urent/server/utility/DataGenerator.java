package com.urent.server.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.domain.*;
import com.urent.server.domain.util.QueryFilter;
import com.urent.server.domains.GetListResult;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.AssertTrue;

import java.io.FileInputStream;
import java.util.*;

import org.junit.Assert;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by Administrator on 2015/8/24.
 * 本类用于测试时生成各种数据
 */
public class DataGenerator {
    public static String administratorMobile = "18612345678";
    public static String administratorPassword = "filename";
    public static String lockKeeperMobile = "13000000001";
    public static String lockKeeperPassword = "seeyounexttime";

    private static String start = "start";
    private static String limit = "limit";
    private static String filter = "filter";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private MockHttpSession adminSession;



    public DataGenerator(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;

        loginAsAdmin();
    }

    public MockHttpSession getAdminSession() {
        return adminSession;
    }

    public  MockHttpSession login(User user)throws Exception{
        LoginInfo loginInfo=new LoginInfo();
        if(user == null) {
            // 使用一个预先存在数据库中的用户名和密码
            user = new User();
            user.setMobile("13000000002");
            user.setPassword("123456");
        }
        loginInfo.setUsername(user.getMobile());
        loginInfo.setPassword(user.getPassword());
        loginInfo.setDevice("Android: test-session");
        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));
        return (MockHttpSession) actions.andReturn().getRequest().getSession();

    }

    private ResultActions register(User user, String headerImageFileName) throws Exception {
        RegisterInfo registerInfo = new RegisterInfo();
        registerInfo.setUser(user);
        registerInfo.setRegisterCode(RandomFactory.getNum(10000, 99999));
        registerInfo.setDevice("Android: test-session");
        String json = objectMapper.writeValueAsString(registerInfo);

        FileInputStream stream = new FileInputStream(headerImageFileName);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", headerImageFileName, "image/png", stream);
        MockMultipartFile jsonPart = new MockMultipartFile("registerInfo", "", "application/json", json.getBytes());

        HashMap<String, String> contentTypeParams = new HashMap<String, String>();
        contentTypeParams.put("boundary", "265001916915724");
        MediaType mediaType = new MediaType("multipart", "form-data", contentTypeParams);

        return mockMvc.perform(MockMvcRequestBuilders.fileUpload("/register").file(mockMultipartFile).file(jsonPart).contentType(mediaType));
    }

    private void loginAsAdmin() throws Exception {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(administratorMobile);
        loginInfo.setPassword(administratorPassword);
        loginInfo.setDevice("Android: test-session");

        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));

        this.adminSession = (MockHttpSession) actions.andReturn().getRequest().getSession();
    }


    public User getOneUser() throws Exception {
        ResultActions actions = mockMvc.perform((get("/user2").session(adminSession).contentType(MediaType.APPLICATION_JSON).
                param(start, "0").param(limit, "625")));

        actions.andExpect(status().isOk());
        String result = actions.andReturn().getResponse().getContentAsString();
        GetListResult<User> listResult = objectMapper.readValue(result, new TypeReference<GetListResult<User>>() {
        });

        if(listResult.getList().size() > 0) {
            int index = RandomFactory.getNum(0, listResult.getList().size());
            return listResult.getList().get(index);
        }
        else {
            return null;
        }
    }

    public Address getOneAddress() throws Exception {

        ResultActions actions = mockMvc.perform((get("/address").session(adminSession).contentType(MediaType.APPLICATION_JSON).
                param(start, "0").param(limit, "625")));

        actions.andExpect(status().isOk());
        String result = actions.andReturn().getResponse().getContentAsString();
        GetListResult<Address> listResult = objectMapper.readValue(result, new TypeReference<GetListResult<Address>>() {
        });

        if(listResult.getList().size() > 0) {
            int index = RandomFactory.getNum(0, listResult.getList().size());
            return listResult.getList().get(index);
        }
        else {
            return null;
        }
    }


    public House getOneHouse() throws Exception {
        ResultActions actions = mockMvc.perform((get("/house").session(adminSession).contentType(MediaType.APPLICATION_JSON).
                param(start, "0").param(limit, "625")));

        actions.andExpect(status().isOk());
        String result = actions.andReturn().getResponse().getContentAsString();
        GetListResult<House> listResult = objectMapper.readValue(result, new TypeReference<GetListResult<House>>() {
        });

        if(listResult.getList().size() > 0) {
            int index = RandomFactory.getNum(0, listResult.getList().size());
            return listResult.getList().get(index);
        }
        else {
            return null;
        }
    }


    public LockType getOneLockType() throws Exception {
        ResultActions actions = mockMvc.perform((get("/lockType").session(adminSession).contentType(MediaType.APPLICATION_JSON).
                param(start, "0").param(limit, "625")));

        actions.andExpect(status().isOk());
        String result = actions.andReturn().getResponse().getContentAsString();
        GetListResult<LockType> listResult = objectMapper.readValue(result, new TypeReference<GetListResult<LockType>>() {
        });

        if(listResult.getList().size() > 0) {
            int index = RandomFactory.getNum(0, listResult.getList().size());
            return listResult.getList().get(index);
        }
        else {
            return null;
        }
    }

    public Key getOneSlaveKey() throws Exception {
        loginAsAdmin();
        ResultActions actions=mockMvc.perform(get("/key").session(adminSession).contentType(MediaType.APPLICATION_JSON).param(start, "0").param(limit, "625"))
                .andExpect(status().isOk());
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        List<Key> list=listResult.getList();
        List<Key> primaryKeyList=new ArrayList<Key>();
        for (Iterator<Key> it=list.iterator();it.hasNext();) {
            Key key=it.next();
            if (key.getType().equals("slave"))
                primaryKeyList.add(key);
        }
        int total=primaryKeyList.size();
        int num=RandomFactory.getNum(0,total);
        Key key=primaryKeyList.get(num);
        return key;
    }

    public Key getOnePrimaryKeyfromOneUser(User loginUser) throws Exception {
        MockHttpSession session = login(loginUser);
        ResultActions actions=mockMvc.perform(get("/myKey").session(session).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        GetListResult<Key> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Key>>() {
        });

        List<Key> list=listResult.getList();
        List<Key> primaryKeyList=new ArrayList<Key>();
        for (Iterator<Key> it=list.iterator();it.hasNext();) {
            Key key=it.next();
            if (key.getType().equals("primary"))
                primaryKeyList.add(key);
        }
        int total=primaryKeyList.size();
        if (total==0) return  null;
        int num=RandomFactory.getNum(0,total);
        Key key=primaryKeyList.get(num);
        return key;
    }

    public Area getOneLeafArea() throws Exception {
        // 随机选择一个Area
        List<QueryFilter> list = new ArrayList<QueryFilter>(1);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("level");
        queryFilter.setValue(4);
        list.add(queryFilter);

        ResultActions actions = mockMvc.perform((get("/area").session(adminSession).contentType(MediaType.APPLICATION_JSON).
                param(start, "0").param(limit, "625").param(filter, FilterMaker.makeFilter(objectMapper, list))));

        actions.andExpect(status().isOk());
        String result = actions.andReturn().getResponse().getContentAsString();
        GetListResult<Area> listResult = objectMapper.readValue(result, new TypeReference<GetListResult<Area>>() {
        });

        if(listResult.getList().size() > 0) {

            int index = RandomFactory.getNum(0, listResult.getList().size());
            return listResult.getList().get(index);
        }
        else {
            return null;
        }
    }

    public User generateUser() throws Exception {
        User user = RandomFactory.getUser();

        ResultActions actions = register(user, "src/test/resources/mylittledaughter.png").andExpect(status().isOk());

        User user2 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), User.class);
        user2.setPassword(user.getPassword());
        return user2;
    }

    public Address generateAddress(Area area) throws Exception {
        Address address = new Address();
        if (area == null) {
            area = getOneLeafArea();
        }
        address.setArea(area);
        String subdistrict = RandomFactory.getString(3);
        subdistrict += "新村";
        address.setSubdistrict(subdistrict);
        address.setLatitude((float) RandomFactory.getNum(0, 90));
        address.setLongitude((float) RandomFactory.getNum(0, 180));

        ResultActions actions = mockMvc.perform(post("/address").session(adminSession).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)));
        actions.andExpect(status().isOk());
        Address address1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Address.class);

        return address1;
    }

    public House generateHouse(Address address, User owner) throws Exception {
        House house = new House();
        if(address == null) {
            // 随机选择一Address
            address = generateAddress(null);
        }

        if(owner == null) {
            // 房屋的Owner是可能为空
        }

        house.setAddress(address);
        house.setOwner(owner);
        house.setBuilding(String.valueOf(RandomFactory.getNum(1, 32)));
        house.setFloor(RandomFactory.getNum(1, 15));
        house.setUnit(String.valueOf(RandomFactory.getNum(1, 5)));
        house.setNumber(String.valueOf(house.getFloor() * 100 + RandomFactory.getNum(0, 4)));

        ResultActions actions = mockMvc.perform(post("/house").session(adminSession).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(house)));
        actions.andExpect(status().isOk());
        House house11 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), House.class);

        return house11;
    }

    public Lock generateLock(House house, LockType lockType) throws Exception {
        if(house == null) {
            house = generateHouse(null, null);
        }

        if(lockType == null) {
            lockType = getOneLockType();
        }

        Lock lock = new Lock();
        lock.setActive(true);
        lock.setConstantKeyWordExpiredDate(new Date());
        lock.setGapAddress(RandomFactory.getString(17));
        lock.setHouse(house);
        lock.setType(lockType);
        //测试数据(写死)
        Version version = new Version();
        version.setId(3L);
        version.setMajor((short) 1);
        version.setMinor((short)1);
        lock.setCurrentFirmwareVersion(version);
        lock.setPowerDensity(80);


        ResultActions actions = mockMvc.perform(post("/lock").session(adminSession).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lock)));
        actions.andExpect(status().isOk());
        Lock lock1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Lock.class);

        return lock1;
    }

    public Key generatePrimaryOrTempKey(Lock lock, User owner, String type) throws Exception {
        if(lock == null) {
            lock = generateLock(null, null);
        }
        if(owner == null) {
            owner = generateUser();
        }

        Key key = new Key();
        key.setStatus(Key.statusActive);
        key.setLock(lock);
        key.setOwner(owner);
        key.setType(type);
        if(type.equals(Key.typePrimary)) {

        }
        else if(type.equals(Key.typeTemp)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 1);
            key.setExpiredDate(calendar.getTime());
        }


        ResultActions actions = mockMvc.perform(post("/key").session(adminSession).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(key)));
        actions.andExpect(status().isOk());
        Key key1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Key.class);

        return key1;
    }

    public Key generateSlaveKey(Key primary, User owner, Date expiredDate, MockHttpSession userSession) throws Exception {
        if(primary == null || owner == null || expiredDate == null)
            throw new RuntimeException("特定域不能为空");

        Key key = new Key();

        // 奇葩的接口
        key.setExpiredDate(expiredDate);
        key.setOwner(owner);
        key.setId(primary.getId());



        ResultActions actions = mockMvc.perform(post("/share").session(userSession).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(key)));
        actions.andExpect(status().isOk());
        Key key1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Key.class);

        return key1;


    }


    public String getListResult( String url, Integer start, Integer limit, List<QueryFilter> filters) throws Exception {
        String filterText = objectMapper.writeValueAsString(filters);

        ResultActions actions = mockMvc.perform(get(url).session(adminSession).contentType(MediaType.APPLICATION_JSON)
                .param("start", String.valueOf(start))
                .param("limit", String.valueOf(limit))
                .param("filter", filterText));
        actions.andExpect(status().isOk());

        return actions.andReturn().getResponse().getContentAsString();
    }

    public String getDomainById(String url, Long id) throws Exception {
        String url2 = url + "/" + String.valueOf(id);

        ResultActions actions = mockMvc.perform(get(url2).session(adminSession).contentType(MediaType.APPLICATION_JSON));
        actions.andExpect(status().isOk());

        return actions.andReturn().getResponse().getContentAsString();
    }


    public void unlock(Key key, MockHttpSession session) throws Exception {
        boolean bGetKeyWord = true;
        if(!key.getType().equals(Key.typeTemp)) {
            // 非临时钥匙有百分之一的机率去取原语
            int i = RandomFactory.getNum(1,1000);
            if(i > 10) {
                bGetKeyWord = false;
            }
        }

        if(bGetKeyWord) {
            WordInfo wordInfo = new WordInfo();
            if(key.getType().equals(Key.typeTemp)) {
                wordInfo.setDisposableLockWord(RandomFactory.getString(16));
            }
            else {
                wordInfo.setConstantLockWord(RandomFactory.getString(16));
            }
            wordInfo.setKeyId(key.getId());

            ResultActions actions =  mockMvc.perform(post("/myKey/keyWord").session(session).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(wordInfo)));

            actions.andExpect(status().isOk());


            WordInfo wordInfo11 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), WordInfo.class);
            if(key.getType().equals(Key.typeTemp)) {
                Assert.assertNotNull(wordInfo11.getDisposableKeyWord());
                Assert.assertEquals(wordInfo11.getDisposableKeyWord().length(), 16);
            }
            else {
                Assert.assertNotNull(wordInfo11.getConstantKeyWord());
                Assert.assertEquals(wordInfo11.getConstantKeyWord().length(), 16);
            }
        }


        // 汇报开门成功
        Thread.sleep(1000);
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(key.getId());
        usedInfo.setTime(new Date());
        usedInfo.setVersion("1.00001");
        usedInfo.setPowerDensity(80);

        List<UsedInfo> list = new ArrayList<>(1);
        list.add(usedInfo);


        ResultActions actions =  mockMvc.perform(post("/myKey/keyUsed").session(session).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));

        actions.andExpect(status().isOk());
    }


    public void tempKeyLeave(Key key, MockHttpSession session) throws Exception {
        UsedInfo usedInfo = new UsedInfo();
        usedInfo.setKeyId(key.getId());
        usedInfo.setTime(new Date());
        usedInfo.setVersion("1.00001");
        usedInfo.setPowerDensity(80);

        List<UsedInfo> list = new ArrayList<>(1);
        list.add(usedInfo);


        ResultActions actions =  mockMvc.perform(post("/myKey/keyUsed").session(session).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(list)));

        actions.andExpect(status().isOk());
    }


    public void deactivateKey(Key key) throws Exception{

        ResultActions actions =  mockMvc.perform(post("/key/deactivate").session(adminSession).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(key)));

        actions.andExpect(status().isOk());

    }

}

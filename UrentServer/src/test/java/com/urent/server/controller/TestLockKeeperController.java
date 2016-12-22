package com.urent.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.urent.server.USException;
import com.urent.server.domain.*;
import com.urent.server.domain.util.QueryFilter;
import com.urent.server.domains.GetListResult;
import com.urent.server.utility.DataGenerator;
import com.urent.server.utility.FilterMaker;
import com.urent.server.utility.RandomFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by Administrator on 2015/8/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:applicationContext.xml")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLockKeeperController {


    private MockMvc mockMvc;


    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private DataGenerator dataGenerator;

    private MockHttpSession lockKeeperSession;


    private void lockKeeperLogin() throws Exception {

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername(DataGenerator.lockKeeperMobile);
        loginInfo.setPassword(DataGenerator.lockKeeperPassword);
        loginInfo.setDevice(RandomFactory.getString(15));

        ResultActions actions = mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).
                content(objectMapper.writeValueAsString(loginInfo)));

        this.lockKeeperSession = (MockHttpSession) actions.andReturn().getRequest().getSession();
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(content().contentType("application/json;charset=UTF-8")).build();
        dataGenerator = new DataGenerator(mockMvc, objectMapper);
        lockKeeperLogin();
    }


    @Test
    public void testLocateLock1() throws Exception {
        String gapAddress = RandomFactory.getString(17);
        mockMvc.perform(get("/locateLock").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("gapAddress", gapAddress)).andExpect(status().isOk()).andExpect(jsonPath("$.total").value(0));

    }

    @Test
    public void testLocateLock2() throws Exception {
        User user = dataGenerator.generateUser();
        Address address = dataGenerator.generateAddress(null);
        House house = dataGenerator.generateHouse(address, user);
        Lock lock = dataGenerator.generateLock(house, null);
        ResultActions actions = mockMvc.perform(get("/locateLock").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("gapAddress", lock.getGapAddress())).andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1));


        GetListResult<Lock> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<Lock>>() {
        });
        Assert.assertEquals(lock.getId(), listResult.getList().get(0).getId());
        Assert.assertEquals(listResult.getTotal(), (Long) 1L);
    }

    @Test
    public void testLocateUser1() throws Exception {
        String mobile = RandomFactory.generateMobile();

        mockMvc.perform(get("/locateUser").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("mobile", mobile)).andExpect(status().isOk()).andExpect(jsonPath("$.total").value(0));

    }

    @Test
    public void testLocateUser2() throws Exception {
        User user = dataGenerator.generateUser();


        ResultActions actions = mockMvc.perform(get("/locateUser").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("mobile", user.getMobile())).andExpect(status().isOk()).andExpect(jsonPath("$.total").value(1));


        GetListResult<User> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<GetListResult<User>>() {
        });
        Assert.assertEquals(user.getId(), listResult.getList().get(0).getId());
        Assert.assertEquals(listResult.getTotal(), (Long) 1L);
    }

    @Test
    public void testLocateLockType1() throws Exception {
        ResultActions actions = mockMvc.perform(get("/locateLockType").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25")).andExpect(status().isOk());

        GetListResult<LockType> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<LockType>>() {
        });
        if(listResult.getTotal() > 0) {
            Assert.assertTrue(listResult.getList().size() > 0 && listResult.getList().size() <=25);
        }
    }

    @Test
    public void testLocateAddress1() throws Exception {
        ResultActions actions = mockMvc.perform(get("/locateAddress").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25")).andExpect(status().isOk());

        GetListResult<Address> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<Address>>() {
                });
        if(listResult.getTotal() > 0) {
            Assert.assertTrue(listResult.getList().size() > 0 && listResult.getList().size() <=25);
        }
    }


    @Test
    public void testLocateAddress2() throws Exception {
        String subdistrict = RandomFactory.getString(5);

        List<QueryFilter> list = new ArrayList<QueryFilter>(1);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("subdistrict");
        queryFilter.setValue(subdistrict);
        list.add(queryFilter);
        String filter = FilterMaker.makeFilter(objectMapper, list);
        ResultActions actions = mockMvc.perform(get("/locateAddress").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        GetListResult<Address> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<Address>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)0L);
    }


    @Test
    public void testLocateAddress3() throws Exception {
        Address address = dataGenerator.generateAddress(null);

        List<QueryFilter> list = new ArrayList<QueryFilter>(1);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("subdistrict");
        queryFilter.setValue(address.getSubdistrict());
        list.add(queryFilter);
        String filter = FilterMaker.makeFilter(objectMapper, list);
        ResultActions actions = mockMvc.perform(get("/locateAddress").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        GetListResult<Address> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<Address>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)1L);
        Assert.assertEquals(listResult.getList().get(0).getId(), address.getId());
    }

    @Test
    public void testLocateHouse1() throws Exception {
        ResultActions actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25")).andExpect(status().isOk());

        GetListResult<House> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        if(listResult.getTotal() > 0) {
            Assert.assertTrue(listResult.getList().size() > 0 && listResult.getList().size() <=25);
        }
    }


    @Test
    public void testLocateHouse2() throws Exception {
        String subdistrict = RandomFactory.getString(5);

        List<QueryFilter> list = new ArrayList<QueryFilter>(1);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("subdistrict");
        queryFilter.setValue(subdistrict);
        list.add(queryFilter);
        String filter = FilterMaker.makeFilter(objectMapper, list);
        ResultActions actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        GetListResult<House> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)0L);
    }


    @Test
    public void testLocateHouse3() throws Exception {
        Address address = dataGenerator.generateAddress(null);
        House house = dataGenerator.generateHouse(address, null);

        List<QueryFilter> list = new ArrayList<QueryFilter>(1);
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("subdistrict");
        queryFilter.setValue(address.getSubdistrict());
        list.add(queryFilter);
        String filter = FilterMaker.makeFilter(objectMapper, list);
        ResultActions actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        GetListResult<House> listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)1L);
        Assert.assertEquals(listResult.getList().get(0).getId(), house.getId());

        queryFilter = new QueryFilter();
        queryFilter.setProperty("building");
        queryFilter.setValue(house.getBuilding());
        list.add(queryFilter);
        filter = FilterMaker.makeFilter(objectMapper, list);

        actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)1L);
        Assert.assertEquals(listResult.getList().get(0).getId(), house.getId());


        queryFilter = new QueryFilter();
        queryFilter.setProperty("unit");
        queryFilter.setValue(house.getUnit());
        list.add(queryFilter);
        filter = FilterMaker.makeFilter(objectMapper, list);

        actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)1L);
        Assert.assertEquals(listResult.getList().get(0).getId(), house.getId());


        queryFilter = new QueryFilter();
        queryFilter.setProperty("number");
        queryFilter.setValue(house.getNumber());
        list.add(queryFilter);
        filter = FilterMaker.makeFilter(objectMapper, list);

        actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)1L);
        Assert.assertEquals(listResult.getList().get(0).getId(), house.getId());

        list.remove(queryFilter);
        queryFilter = new QueryFilter();
        queryFilter.setProperty("number");
        queryFilter.setValue(house.getNumber() + "aaaaa");
        list.add(queryFilter);
        filter = FilterMaker.makeFilter(objectMapper, list);

        actions = mockMvc.perform(get("/locateHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                param("start", "0").param("limit", "25").param("filter", filter)).andExpect(status().isOk());

        listResult = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(),
                new TypeReference<GetListResult<House>>() {
                });
        Assert.assertEquals(listResult.getTotal(), (Long)0L);
    }

    @Test
    public void testRegisterHouse1() throws Exception {
        Address address = dataGenerator.generateAddress(null);
        House house = new House();

        house.setAddress(address);
        house.setBuilding(String.valueOf(RandomFactory.getNum(1, 32)));
        house.setFloor(RandomFactory.getNum(1, 15));
        house.setUnit(String.valueOf(RandomFactory.getNum(1, 5)));
        house.setNumber(String.valueOf(house.getFloor() * 100 + RandomFactory.getNum(0, 4)));

        String jsonString = objectMapper.writeValueAsString(house);
        ResultActions actions = mockMvc.perform(post("/registerHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk());
        House house1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), House.class);
        Assert.assertNotEquals(house1.getId(), null);
    }


    @Test
    public void testRegisterHouse2() throws Exception {
        Address address = dataGenerator.generateAddress(null);
        House house = new House();

        house.setAddress(address);
        house.setBuilding(String.valueOf(RandomFactory.getNum(1, 32)));
        house.setFloor(RandomFactory.getNum(1, 15));
        house.setUnit(String.valueOf(RandomFactory.getNum(1, 5)));
        house.setNumber(String.valueOf(house.getFloor() * 100 + RandomFactory.getNum(0, 4)));

        String jsonString = objectMapper.writeValueAsString(house);
        ResultActions actions = mockMvc.perform(post("/registerHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk());
        House house1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), House.class);
        Assert.assertNotEquals(house1.getId(), null);

        mockMvc.perform(post("/registerHouse").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isInternalServerError()).andExpect(jsonPath("$.errorCode").value(USException.ErrorCode.HouseWithSameAddressExists.getCode()));
    }

    @Test
    public void testRegisterLock1() throws Exception {
        House house = dataGenerator.generateHouse(null, null);
        Lock lock = new Lock();
        lock.setType(dataGenerator.getOneLockType());
        lock.setHouse(house);
        lock.setGapAddress(RandomFactory.getString(17));
        lock.setActive(true);

        Version version = new Version();
        version.setMajor((short)1);
        version.setMinor((short)1);
        lock.setCurrentFirmwareVersion(version);

        String jsonString = objectMapper.writeValueAsString(lock);
        ResultActions actions = mockMvc.perform(post("/registerLock").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk());
        Lock lock1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Lock.class);
        Assert.assertNotNull(lock1.getId());
        Assert.assertNotNull(lock1.getConstantEncryptWord());
        Assert.assertNotNull(lock1.getDisposableEncryptWord());

        List<QueryFilter> list = new ArrayList<QueryFilter>();
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("gapAddress");
        queryFilter.setValue(lock.getGapAddress());
        list.add(queryFilter);
        GetListResult<Lock> result = objectMapper.readValue(dataGenerator.getListResult("/lock", 0, 25, list), new TypeReference<GetListResult<Lock>>() {
        });
        Assert.assertEquals(result.getTotal(), (Long)1L);
        Assert.assertEquals(result.getList().get(0).getId(), lock1.getId());

        Lock lock2 = objectMapper.readValue(dataGenerator.getDomainById("/lock", lock1.getId()), Lock.class);
        Assert.assertEquals(lock2.getId(), lock1.getId());
        Assert.assertEquals(lock2.getGapAddress(), lock.getGapAddress());
        Assert.assertEquals(lock2.getHouse().getId(), house.getId());

        // 再确认一下没有生成关于这把锁的钥匙
        list.clear();
        queryFilter = new QueryFilter();
        queryFilter.setProperty("lockId");
        queryFilter.setValue(lock1.getId());
        list.add(queryFilter);
        GetListResult<Key> result2 = objectMapper.readValue(dataGenerator.getListResult("/key", 0, 25, list), new TypeReference<GetListResult<Key>>() {
        });
        Assert.assertEquals(result2.getTotal(), (Long)0L);
    }

    @Test
    public void testRegisterLock2() throws Exception {
        User user = dataGenerator.generateUser();
        House house = dataGenerator.generateHouse(null, user);
        Lock lock = new Lock();
        lock.setType(dataGenerator.getOneLockType());
        lock.setHouse(house);
        lock.setGapAddress(RandomFactory.getString(17));
        lock.setActive(true);


        Version version = new Version();
        version.setMajor((short)1);
        version.setMinor((short)1);
        lock.setCurrentFirmwareVersion(version);

        String jsonString = objectMapper.writeValueAsString(lock);
        ResultActions actions = mockMvc.perform(post("/registerLock").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk());
        Lock lock1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Lock.class);
        Assert.assertNotNull(lock1.getId());
        Assert.assertNotNull(lock1.getConstantEncryptWord());
        Assert.assertNotNull(lock1.getDisposableEncryptWord());

        List<QueryFilter> list = new ArrayList<QueryFilter>();
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("gapAddress");
        queryFilter.setValue(lock.getGapAddress());
        list.add(queryFilter);
        GetListResult<Lock> result = objectMapper.readValue(dataGenerator.getListResult("/lock", 0, 25, list), new TypeReference<GetListResult<Lock>>() {
        });
        Assert.assertEquals(result.getTotal(), (Long)1L);
        Assert.assertEquals(result.getList().get(0).getId(), lock1.getId());

        Lock lock2 = objectMapper.readValue(dataGenerator.getDomainById("/lock", lock1.getId()), Lock.class);
        Assert.assertEquals(lock2.getId(), lock1.getId());
        Assert.assertEquals(lock2.getGapAddress(), lock.getGapAddress());
        Assert.assertEquals(lock2.getHouse().getId(), house.getId());

        // 确认一下为房主生成了关于这把锁的钥匙
        list.clear();
        queryFilter = new QueryFilter();
        queryFilter.setProperty("lockId");
        queryFilter.setValue(lock1.getId());
        list.add(queryFilter);
        GetListResult<Key> result2 = objectMapper.readValue(dataGenerator.getListResult("/key", 0, 25, list), new TypeReference<GetListResult<Key>>() {
        });
        Assert.assertEquals(result2.getTotal(), (Long)1L);
        Key key = result2.getList().get(0);

        Assert.assertEquals(key.getOwner().getId(), user.getId());
        Assert.assertEquals(key.getType(), Key.typePrimary);
    }

    @Test
    public void testSetOwner1() throws Exception {
        House house = dataGenerator.generateHouse(null, null);
        Lock lock = new Lock();
        lock.setType(dataGenerator.getOneLockType());
        lock.setHouse(house);
        lock.setGapAddress(RandomFactory.getString(17));
        lock.setActive(true);
        Version version = new Version();
        version.setMajor((short) 1);
        version.setMinor((short) 1);
        lock.setCurrentFirmwareVersion(version);

        String jsonString = objectMapper.writeValueAsString(lock);
        ResultActions actions = mockMvc.perform(post("/registerLock").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk());
        Lock lock1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Lock.class);
        Assert.assertNotNull(lock1.getId());
        Assert.assertNotNull(lock1.getConstantEncryptWord());
        Assert.assertNotNull(lock1.getDisposableEncryptWord());

        User user = dataGenerator.generateUser();
        lock1.getHouse().setOwner(user);
        jsonString = objectMapper.writeValueAsString(lock1);
        actions = mockMvc.perform(post("/setOwner").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));

        // 确认一下为房主生成了关于这把锁的钥匙
        List<QueryFilter> list = new ArrayList<QueryFilter>();
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setProperty("lockId");
        queryFilter.setValue(lock1.getId());
        list.add(queryFilter);
        GetListResult<Key> result2 = objectMapper.readValue(dataGenerator.getListResult("/key", 0, 25, list), new TypeReference<GetListResult<Key>>() {
        });
        Assert.assertEquals(result2.getTotal(), (Long)1L);
        Key key = result2.getList().get(0);

        Assert.assertEquals(key.getOwner().getId(), user.getId());
        Assert.assertEquals(key.getType(), Key.typePrimary);
    }

    @Test
    public void testSetOwner2() throws Exception {
        User user = dataGenerator.generateUser();
        House house = dataGenerator.generateHouse(null, user);
        Lock lock = new Lock();
        lock.setType(dataGenerator.getOneLockType());
        lock.setHouse(house);
        lock.setGapAddress(RandomFactory.getString(17));
        lock.setActive(true);
        Version version = new Version();
        version.setMajor((short) 1);
        version.setMinor((short) 1);
        lock.setCurrentFirmwareVersion(version);

        String jsonString = objectMapper.writeValueAsString(lock);
        ResultActions actions = mockMvc.perform(post("/registerLock").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isOk());
        Lock lock1 = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Lock.class);
        Assert.assertNotNull(lock1.getId());
        Assert.assertNotNull(lock1.getConstantEncryptWord());
        Assert.assertNotNull(lock1.getDisposableEncryptWord());


        lock1.getHouse().setOwner(user);
        jsonString = objectMapper.writeValueAsString(lock1);
        actions = mockMvc.perform(post("/setOwner").session(lockKeeperSession).contentType(MediaType.APPLICATION_JSON).
                content(jsonString)).andExpect(status().isInternalServerError()).
                andExpect(jsonPath("$.errorCode").value(USException.ErrorCode.OwnerExistsOnHouse.getCode()));
    }
}

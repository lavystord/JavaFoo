package com.urent.server.persistence;

import com.urent.server.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xc
 * Date: 14-3-12
 * Time: 下午4:27
 * To change this template use File | Settings | File Templates.
 */
public interface UserMapper {

    public List<User> getUserList(Map<String, Object> map);

    public long getUserCount(Map<String, Object> map);

    public  User getUserById(long id);

    public User getUserByMobile(String mobile);

    public User getUserByIdCardNumber(String idCardNumber);

    public int addUser(User user);

    public void updateUser(User user);
}

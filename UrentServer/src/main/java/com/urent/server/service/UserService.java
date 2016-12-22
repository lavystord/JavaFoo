package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.LoginInfo;
import com.urent.server.domain.UpdateMobileInfo;
import com.urent.server.domain.UpdatePasswordInfo;
import com.urent.server.domain.User;
import com.urent.server.persistence.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.urent.server.util.PatternCheckTool;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/23.
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Transactional
    public User addUser(User user) {
        // 这里手动检查一下mobile的唯一性，并抛出异常
        if(userMapper.getUserByMobile(user.getMobile()) != null) {
            throw new USException(USException.ErrorCode.UserWithSameMobileExists);
        }
        user.setCreateDate(new Date());
        user.setUpdateDate(user.getCreateDate());
        user.setActive(true);
        user.setMessageOption(User.messageReceiveAll);
        userMapper.addUser(user);

        return user;
    }

    public List<User> getUserList(Map<String, Object> queryFilter) {
        return userMapper.getUserList(queryFilter);
    }

    public User getUserByMobile(String mobile) {
        return userMapper.getUserByMobile(mobile);
    }

    public List<User> locateUserByMobile(String mobile) {
        Map<String, Object> queryFilter = new HashMap<String, Object>(1);
        queryFilter.put("mobile", mobile);

        return userMapper.getUserList(queryFilter);
    }

    public Long getUserCount(Map<String, Object> queryFilter) {
        return userMapper.getUserCount(queryFilter);
    }

    public User getUserById(long id){
        return userMapper.getUserById(id);
    }

    public User updateUser(User user) {
        userMapper.updateUser(user);
        return user;
    }

    public User checkLogin(LoginInfo loginInfo) {
        User user = null;
        if (PatternCheckTool.isMobile(loginInfo.getUsername())) {
            user = userMapper.getUserByMobile(loginInfo.getUsername());
        }
//        废除身份证号登录 by lavystord
//        else if (isIdCard(loginInfo.getUsername())) {
//            user = userMapper.getUserByIdCardNumber(loginInfo.getUsername());
//        }
        else {
            throw new USException(USException.ErrorCode.IllegalDataFormat, "登陆帐号必须使用手机号");
        }

        if (user == null) {
            throw new USException(USException.ErrorCode.NoSuchUser);
        } else if (loginInfo.getPassword().equals(user.getPassword())) {
            if (user.getLastLoginDevice() != null) {
                if (!user.getLastLoginDevice().equals(loginInfo.getDevice())) {
                    // todo
                    User user1 = new User();
                    user1.setId(user.getId());
                    user1.setLastLoginDevice(loginInfo.getDevice());
                    userMapper.updateUser(user1);
                    throw new USException(USException.ErrorCode.DeviceIsAmbiguous);
                }
            }
            else {
                User user1 = new User();
                user1.setId(user.getId());
                user1.setLastLoginDevice(loginInfo.getDevice());
                userMapper.updateUser(user1);
            }


            return user;
        }
        else {
            throw new USException(USException.ErrorCode.PasswordVerifyError, "登陆的密码与用户名不匹配");
        }
    }


    public void updatePassword(User user, UpdatePasswordInfo updatePasswordInfo) {
        User user2 = new User();
        user2.setId(user.getId());
        user2.setPassword(updatePasswordInfo.getNewPassword());

        userMapper.updateUser(user2);
    }

    public void updateMobile(User user, UpdateMobileInfo updateMobileInfo) {
        if(!PatternCheckTool.isMobile(updateMobileInfo.getMobile())) {
            throw new USException(USException.ErrorCode.IllegalDataFormat, "手机号格式非法");
        }
        User user1 = userMapper.getUserById(user.getId());
        if(!user1.getPassword().equals(updateMobileInfo.getPassword())) {
            throw new USException(USException.ErrorCode.PasswordVerifyError);
        }
        User user2 = new User();
        user2.setId(user.getId());
        user2.setMobile(updateMobileInfo.getMobile());

        userMapper.updateUser(user2);
    }

    public void updateIdInfo(User user) {
        User oneUser = new User();
        oneUser.setId(user.getId());
        oneUser.setName(user.getName());
        oneUser.setIdCardNumber(user.getIdCardNumber());
        oneUser.setVerificationStatus(User.vs_authorized);
        userMapper.updateUser(oneUser);
    }




}

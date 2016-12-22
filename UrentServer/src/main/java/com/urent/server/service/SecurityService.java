package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Key;
import com.urent.server.domain.Lock;
import com.urent.server.domain.User;
import com.urent.server.persistence.LockMapper;
import com.urent.server.persistence.MyKeyMapper;
import com.urent.server.persistence.UserMapper;
import com.urent.server.util.PatternCheckTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */

@Service
public class SecurityService {
    @Autowired
    LockMapper lockMapper;

    @Autowired
    MyKeyMapper myKeyMapper;

    @Autowired
    UserMapper userMapper;


   public  void resetConstantKeyWord (String username) {
       User user = null;
       if (PatternCheckTool.isMobile(username)) {
           user = userMapper.getUserByMobile(username);
           resetConstantKeyWord(user);
       }
       else {
           throw new USException(USException.ErrorCode.IllegalDataFormat, "登陆帐号格式有误");
       }

   }

    public void resetConstantKeyWord(User oneUser) {
        if (oneUser==null)
            throw new USException(USException.ErrorCode.NoSuchUser);

       List<Key> list=myKeyMapper.getPrimaryAndSlaveKeysOfOneUser(oneUser.getId());
        for (Iterator<Key> it=list.iterator();it.hasNext();) {
            resetConstantKeyWord(it.next());
        }
    }

    public void resetConstantKeyWord(Key oneKey) {
        Lock lock = oneKey.getLock();
        Date current = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        calendar.add(Calendar.MONTH,-1);
        Date date = calendar.getTime();

        lock.setConstantKeyWordExpiredDate(date);
        lock.setUpdateDate(current);
        lockMapper.updateLock(lock);
    }

}

package com.urent.server.service;

import com.urent.server.USException;
import com.urent.server.domain.Key;
import com.urent.server.domain.User;
import com.urent.server.persistence.KeyMapper;
import com.urent.server.persistence.MyKeyMapper;
import com.urent.server.persistence.UserMapper;
import com.urent.server.util.KeyIsExpired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Dell on 2015/10/15.
 */
@Service
public class KeyAuthorityVerifyService {
    @Autowired
    MyKeyMapper myKeyMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    KeyMapper keyMapper;
    /**
     * 判断所请求钥匙和当前登录用户是否一致
     * @param loginUser
     * @param keyId
     * @return
     */
    public   Key authorityCheck(User loginUser,Long keyId,String keyType) {
        Key key;
        key=commonCheck(loginUser,keyId);
//        Key's type check
        if (keyType!=null) {
            if (!key.getType().equals(keyType))
                throw new USException(USException.ErrorCode.ThisKeyCannotDoThisAction);
        }
        else
        {
            keyType="all";
        }

         /*判断keyId所属主人和当前登录用户是否匹配*/
        switch (keyType) {
            case Key.typePrimary:
            case Key.typeTemp:
                if (loginUser.getId().longValue()!=key.getOwner().getId().longValue())
                    throw new USException(USException.ErrorCode.RequestUnauthorized," 该钥匙不属于当前用户");
//                System.out.println("123");
                break;
            case Key.typeSlave:
                if (key.getSharedFrom().getOwner().getId().longValue()!=loginUser.getId().longValue() && loginUser.getId().longValue()!=key.getOwner().getId().longValue())
                    throw new USException(USException.ErrorCode.RequestUnauthorized," 该钥匙不属于当前用户");
//                System.out.println("456");
                break;
            default:
                if (loginUser.getId().longValue()!=key.getOwner().getId().longValue())
                    throw new USException(USException.ErrorCode.RequestUnauthorized," 该钥匙不属于当前用户");
//                System.out.println("hello");

        }


//key=new Key();

        return  key;
    }

    private Key commonCheck(User loginUser,Long keyId) {
        //检查当前登录用户是否有效
        User user=userMapper.getUserById(loginUser.getId());
        if (user==null || !user.getActive())
            throw new USException(USException.ErrorCode.NoSuchUser);

        //检查是否存在所请求钥匙
        Key key=myKeyMapper.getKeyInfoById(keyId);
        if (key==null) {
            throw  new USException(USException.ErrorCode.KeyIsOutdate,"您所请求的钥匙不合法");
        }

        //检查钥匙是否过期
       /* if (key.getExpiredDate()!=null) {
            Date currentDate = new Date();
            if (currentDate.after(key.getExpiredDate())) {
                //expired
                if (key.getType().equals(Key.typeTemp)) {

                    key.setUpdateDate(new Date());
                    keyMapper.tempKeyExpired(key);
                    //
                }
                else {
                    key.setUpdateDate(new Date());
                    keyMapper.deactivateKey(key);
                    throw  new USException(USException.ErrorCode.KeyIsOutdate,"您所请求的钥匙不存在");
                }

            }
        }*/
        if (KeyIsExpired.checkKeyIsExpired(key)) {
            keyMapper.updateKeyStatus(key);
            if (!key.getType().equals(Key.typeTemp))
                throw  new USException(USException.ErrorCode.KeyIsOutdate,"您所请求的钥匙不合法");
        }

        return  key;
    }

   /* public static void main(String[] args) {
        authorityCheck(new User(),1L,"primary");
        System.out.println("----");
        authorityCheck(new User(),1L,"all");
        System.out.println("----");
        authorityCheck(new User(),1L,"slave");
    }*/

}

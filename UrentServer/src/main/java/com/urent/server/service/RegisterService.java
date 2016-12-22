package com.urent.server.service;

import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/7/23.
 */

@Service
public class RegisterService {

    /**
     *  向手机号发送验证码，用于注册用，生成的验证码为int类型，保存10min，10min内可验证
     * @param mobile 手机号
     *
     */
    public void sendRegisterCode(String mobile) {

    }


    /**
     * 验证注册验证码
     * @param mobile  手机号
     * @param registerCode  注册验证码
     * @return 验证是否成功
     */
    public boolean verifyRegisterCode(String mobile, int registerCode, boolean removeAfterVerify) {
        return true;
    }

}

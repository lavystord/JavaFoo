package com.urent.server.util;

import com.urent.server.USException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by Administrator on 2015/8/21.
 */
public class LockWordFactory {
    private static final int lockWordLength = 16;
    private final static int sha1ResultLen = 20;


//    private final static String KEY_SHA = "SHA";
//    private final static String KEY_SHA1 = "SHA-1";
//    /**
//     * 全局数组
//     */
//    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
//            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String generateLockWord() {
        char[] lockWord = new char[16];
        for (int i = 0; i < 16; i ++) {
            lockWord[i] = (char)(Math.random() * 256);
        }
        String s = new String(lockWord);
        return s;
    }


    public static String calculateKeyWord(String lockWord, String encryptWord) {
        char[] lockWordBytes = lockWord.toCharArray();
        char[] encryptWordBytes = encryptWord.toCharArray();

        if(lockWordBytes.length != lockWordLength){
            throw new USException(USException.ErrorCode.IllegalDataFormat, "锁原语的长度必须是" + lockWordLength + "字节");
        }

        byte[] temp = new byte[lockWordLength];

        for(int i = 0; i < lockWordLength; i ++) {
            temp[i] = (byte) (lockWordBytes[i] ^ encryptWordBytes[i]);
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("sha-1");
            digest.update(temp);
            byte[] result = digest.digest();
            assert result.length == sha1ResultLen;
            char[] result2 = new char[lockWordLength];
            for(int i = 0; i < lockWordLength; i ++) {
                result2[i] = (char)result[i];
            }
            return new String(result2);
        } catch (NoSuchAlgorithmException e) {
            throw new USException(USException.ErrorCode.SystemRunEnvironmentError, "找不到相应的加密算法");
        }
    }




    public static void main2(String[] args){
        //String s = calculateKeyWord(generateLockWord(), generateLockWord());
       // System.out.println(s);
       // String disposableEncryptWord="258963147okmnji2";
        String disposableEncryptWord="???ae?6ta\u001B?B\u0016ó\u000B?";
        String constantEncryptWord="?ì2N\\e?J?\u001F?F+(?×";
        String dLockWord="1234567890abcdef";
        String cLockWord="1472583690qwerty";
        String dKeyWord,cKeyWord;
        dKeyWord=calculateKeyWord(dLockWord,disposableEncryptWord);
        cKeyWord=calculateKeyWord(cLockWord,constantEncryptWord);
        System.out.println(dKeyWord);
        System.out.println(cKeyWord);
    }

    private static byte[] stringToBytes(String s) {
        char c[] = s.toCharArray();
        byte b[] = new byte[c.length];
        for(int i = 0; i < c.length; i ++){
            b[i] = (byte)c[i];
        }

        return b;
    }

    private static String bytesToString(byte[] b) {
        char[] c = new char[b.length];
        for(int i = 0; i < b.length; i ++) {
            if (b[i]<0)
                c[i] = (char)(b[i]+256);
            else
                c[i]=(char)b[i];
        }
        return new String(c);
    }

  /*  private static String byteToHexString(byte b) {
        int ret = b;
        //System.out.println("ret = " + ret);
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }
    public static String encryptSHA(String data) throws Exception {

        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return byteArrayToHexString(bytes);
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }
*/

    public static void main(String[] args) throws Exception {
       byte b[] = new byte[16];
        for(int i = 0; i < 16; i++) {
            b[i] = (byte)(Math.random() * 256);
            System.out.print(b[i]);
            System.out.print(",");
        }
        System.out.println();

        String s = bytesToString(b);

        byte[] b2 = stringToBytes(s);
        for(int i = 0; i < 16; i++) {
            System.out.print(b2[i]);
            System.out.print(",");
        }
//        String key = "123";
//        System.out.println(encryptSHA(key));
    }

}

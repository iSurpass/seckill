package com.juniors.miaosha.Util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5两次加密工具包
 * @author Juniors
 */
public class MD5Util {

    /**
     * 原生md5算法
     * @param src
     * @return
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static final String salt = "abcd1234";

    public static String inputToFormPwd(String input){
        String str = "" + salt.charAt(0) + salt.charAt(2) + input + salt.charAt(4);
        return md5(str);
    }

    public static String formPwdToDB(String formPwd,String saltDB){
        String str = "" + saltDB.charAt(0) + saltDB.charAt(2) + formPwd + saltDB.charAt(4);
        return md5(str);
    }

    public static String inputPwdToDB(String input,String saltDB){
        String str = inputToFormPwd(input);
        String dbPwd = formPwdToDB(str,saltDB);
        return dbPwd;
    }

    public static void main(String[] args) {
        System.out.println(inputPwdToDB("123456","1a2b3c4d"));
    }
}

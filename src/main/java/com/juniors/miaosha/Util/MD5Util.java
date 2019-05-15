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

    //示例 salt 值
    public static final String salt = "abcd1234";

    /**
     * 前端Form表单对用户密码进行第一次md5加密
     * @param input
     * @return
     */
    public static String inputToFormPwd(String input){
        //即使salt值泄露 也不会很快破解
        String str = "" + salt.charAt(0) + salt.charAt(2) + input + salt.charAt(4);
        return md5(str);
    }

    /**
     * 由Form表单加过密的密码再次进行一次加密并存入数据库
     * 数据库密码即加过两次密的密码
     * @param formPwd
     * @param saltDB
     * @return
     */
    public static String formPwdToDB(String formPwd,String saltDB){
        String str = "" + saltDB.charAt(0) + saltDB.charAt(2) + formPwd + saltDB.charAt(4);
        return md5(str);
    }

    /**
     * 由用户密码直接保存数据的密码的直接转换
     * @param input
     * @param saltDB 此salt值，是固定的，也是用户的属性之一
     * @return
     */
    public static String inputPwdToDB(String input,String saltDB){
        String str = inputToFormPwd(input);
        String dbPwd = formPwdToDB(str,saltDB);
        return dbPwd;
    }

    public static void main(String[] args) {
        System.out.println(inputPwdToDB("123456","1a2b3c4d"));
    }
}

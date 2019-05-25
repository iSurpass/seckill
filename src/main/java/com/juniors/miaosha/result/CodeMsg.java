package com.juniors.miaosha.result;

/**
 *
 * @author Juniors
 */
public class CodeMsg {

    private int code;

    private String msg;

    //通常异常 5001XX
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102,"请求非法");
    public static CodeMsg ACCESS_LIMIT = new CodeMsg(500103,"请求频繁");

    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210,"Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"登录手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213,"登录手机号格式错误");
    public static CodeMsg USER_NOT_EXIST = new CodeMsg(500214,"登录用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"登录密码错误");

    //商品模块 5003XX

    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400,"订单不存在");

    //秒杀模块 5005XX
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500,"商品已经秒杀完毕");
    public static CodeMsg REPEAT_ERROR = new CodeMsg(500501,"商品不能重复秒杀");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500501,"秒杀失败");

    public CodeMsg(int i, String msg) {
        this.code = i;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object... args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

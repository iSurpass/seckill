package com.juniors.miaosha.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 前台传来用户登录From表单类
 * @author Juniors
 */
@Setter
@Getter
@ToString
public class LoginVo {

    private String  mobile;

    private String password;

    public String getMobile() {
        return mobile;
    }
}

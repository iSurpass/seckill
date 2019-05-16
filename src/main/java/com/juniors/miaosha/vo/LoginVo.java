package com.juniors.miaosha.vo;

import com.juniors.miaosha.validator.IsMobile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 前台传来用户登录From表单类
 * @author Juniors
 */
@Setter
@Getter
@ToString
public class LoginVo {

    //校验器注释
    @NotNull
    @IsMobile
    private String  mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}

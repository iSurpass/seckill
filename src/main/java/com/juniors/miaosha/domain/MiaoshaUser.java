package com.juniors.miaosha.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 秒杀用户数据类
 * @author Juniors
 */
@Setter
@Getter
@ToString
public class MiaoshaUser {

    private Long id;

    private String nickname;

    private String password;

    private String salt;

    private String head;

    private Date registerDate;

    private Date lastLoginDate;

    private Integer loginCount;
}

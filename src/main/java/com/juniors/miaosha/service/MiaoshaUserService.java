package com.juniors.miaosha.service;

import com.juniors.miaosha.Util.MD5Util;
import com.juniors.miaosha.dao.MiaoshaUserDao;
import com.juniors.miaosha.domain.MiaoshaUser;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 秒杀项目---用户服务类
 * @author Juniors
 */
@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    public MiaoshaUser getById(long id){

        return miaoshaUserDao.getById(id);

    }

    /**
     *
     * @param loginVo
     * @return
     */
    public CodeMsg login(LoginVo loginVo) {

        if (loginVo == null){
            return CodeMsg.SERVER_ERROR;
        }
        String mobile = loginVo.getMobile();
        String pwd = loginVo.getPassword();

        //判断手机号是否存在
        MiaoshaUser user = miaoshaUserDao.getById(Long.parseLong(mobile));
        if (user == null){
            return CodeMsg.USER_NOT_EXIST;
        }

        //验证密码
        String pwdDB = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPwdToDB(pwd,saltDB);
        if (!calcPass.equals(pwdDB)){
            return CodeMsg.PASSWORD_ERROR;
        }
        return CodeMsg.SUCCESS;
    }
}

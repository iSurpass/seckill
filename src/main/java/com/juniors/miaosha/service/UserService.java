package com.juniors.miaosha.service;

import com.juniors.miaosha.dao.UserDao;
import com.juniors.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Juniors
 */
@Service
public class UserService {

    //Service 注入 Dao
    @Autowired
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    /**
     * 检验数据库事务是否正确回滚
     * 要点：@Transactional
     * @return
     */
    @Transactional
    public boolean transaction(){
        User user1 = new User();
        user1.setId(2);
        user1.setName("Harden");
        userDao.insertTest(user1);

        User user2 = new User();
        user2.setId(0);
        user2.setName("Kim");
        userDao.insertTest(user2);

        return true;
    }
}

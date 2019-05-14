package com.juniors.miaosha.controller;

import com.juniors.miaosha.domain.User;
import com.juniors.miaosha.redis.UserKey;
import com.juniors.miaosha.result.CodeMsg;
import com.juniors.miaosha.result.Result;
import com.juniors.miaosha.redis.RedisService;
import com.juniors.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juniors
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){

        model.addAttribute("name","Juniors");
        return "hello";
    }

    @RequestMapping(path = "/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("Skr");
        //return new Result(0,"success","Skr");      优化前的语句逻辑，这就是使用 泛型 的好处
    }

    @RequestMapping(path = "/helloError")
    @ResponseBody
    public Result<String> helloError(){
        return Result.error(CodeMsg.SERVER_ERROR);
        //return new Result(0,"error");
    }

    @RequestMapping(path = "/db/get")
    @ResponseBody
    public Result<User> dbGet(){

        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping(path = "/db/insert")
    @ResponseBody
    public Result<Boolean> dbInsert(){

        userService.transaction();
        return Result.success(true);
    }

    @RequestMapping(path = "/redis/get")
    @ResponseBody
    public Result<User> redisGet(){

        User user = redisService.get(UserKey.getById,""+1,User.class);
        return Result.success(user);
    }

    @RequestMapping(path = "/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){

        User user = new User();
        user.setId(1);
        user.setName("Kobe");
        Boolean b = redisService.set(UserKey.getById,""+user.getId(),user);  //生成id： “UserKey:id1"
        return Result.success(b);
    }
}

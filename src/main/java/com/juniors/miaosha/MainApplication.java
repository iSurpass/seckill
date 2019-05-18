package com.juniors.miaosha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 项目启动类
 * @author Juniors
 */
@SpringBootApplication
public class MainApplication /*extends SpringBootServletInitializer*/ {

    public static void main(String[] args){
        SpringApplication.run(MainApplication.class,args);
    }

    /*继承SpringBootServletInitializer，重写configure函数，以便打war包启动项目
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MainApplication.class);
    }*/
}

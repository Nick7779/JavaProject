package com.nick.nickpicturebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Nick
 */
@SpringBootApplication
@MapperScan("com.nick.nickpicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class NickPictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NickPictureBackendApplication.class, args);
    }

}

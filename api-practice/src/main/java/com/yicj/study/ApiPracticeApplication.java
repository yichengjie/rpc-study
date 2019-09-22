package com.yicj.study;

import com.yicj.study.ioc2.service.IUserService;
import com.yicj.study.ioc2.vo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ApiPracticeApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ApiPracticeApplication.class, args);
        //System.in.read() ;
    }
}

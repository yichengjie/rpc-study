package com.yicj.study.bean1.service.impl;

import com.yicj.study.bean1.service.IHelloService;
import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String username) {
        return "Hello " + username;
    }
}

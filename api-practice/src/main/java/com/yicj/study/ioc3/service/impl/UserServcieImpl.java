package com.yicj.study.ioc3.service.impl;

import com.yicj.study.ioc3.annotation.MyBean;
import com.yicj.study.ioc3.service.IUserService;

@MyBean
public class UserServcieImpl implements IUserService {
    public void hello(String name){
        System.out.println("hello world" + name);
    }
}

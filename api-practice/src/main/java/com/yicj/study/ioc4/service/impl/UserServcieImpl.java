package com.yicj.study.ioc4.service.impl;

import com.yicj.study.ioc4.annotation.MyBean;
import com.yicj.study.ioc4.service.IUserService;

@MyBean
public class UserServcieImpl implements IUserService {
    public void hello(String name){
        System.out.println("hello world" + name);
    }
}

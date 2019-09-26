package com.yicj.study.bean1.common;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Slf4j
public class MyInvocationHandler implements InvocationHandler {
    private Object target ;
    public MyInvocationHandler(Object target){
        this.target = target ;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoke method ...." + method.getName());
        log.info("invoke method before ...." + System.currentTimeMillis());
        Object object = method.invoke(target,args) ;
        log.info("invoke method after ...." + System.currentTimeMillis());
        return object;
    }
}

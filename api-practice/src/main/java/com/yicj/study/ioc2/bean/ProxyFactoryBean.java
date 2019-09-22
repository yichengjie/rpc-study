package com.yicj.study.ioc2.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Proxy;


public class ProxyFactoryBean implements FactoryBean {
    private Class<?> rpcInterface;
    @Autowired
    private MyInvocationHandler myInvocationHandler ;

    public ProxyFactoryBean(Class<?> rpcInterface){
        this.rpcInterface = rpcInterface ;
    }

    @Override
    public Object getObject() throws Exception {
        return getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return rpcInterface;
    }

    public Object getProxy() {
        return  Proxy.newProxyInstance(rpcInterface.getClassLoader(),
                new Class[] { rpcInterface },
                myInvocationHandler);
    }
}

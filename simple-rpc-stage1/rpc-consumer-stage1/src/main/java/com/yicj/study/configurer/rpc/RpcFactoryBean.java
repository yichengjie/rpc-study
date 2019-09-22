package com.yicj.study.configurer.rpc;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class RpcFactoryBean<T> implements FactoryBean {
    private Class<T> rpcInterface;
    @Autowired
    private RpcFactory factory;

    public RpcFactoryBean() {}

    public RpcFactoryBean(Class<T> rpcInterface) {
        this.rpcInterface = rpcInterface;
    }

    public T getObject() throws Exception {
        return getRpc();
    }

    public Class<?> getObjectType() {
        return this.rpcInterface;
    }

    public boolean isSingleton() {
        return true;
    }

    @SuppressWarnings("unchecked")
	public T getRpc() {
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface },factory);
    }
}
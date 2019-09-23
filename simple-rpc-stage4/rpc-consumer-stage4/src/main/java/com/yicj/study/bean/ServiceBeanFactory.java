package com.yicj.study.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

//接口实例工厂，这里主要是用于提供接口的实例对象
public class ServiceBeanFactory<T> implements FactoryBean<T> {

    @Autowired
    private ServiceProxy serviceProxy ;
    private Class<T> interfaceType;
    public ServiceBeanFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    @SuppressWarnings("unchecked")
    //这里主要是创建接口对应的实例，便于注入到spring容器中
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                serviceProxy);
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

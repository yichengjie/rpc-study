package com.yicj.study.bean1.common;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import java.lang.reflect.Proxy;

@Slf4j
public class MyFactoryBean implements FactoryBean<Object>, InitializingBean, DisposableBean {
    @Setter
    private String interfaceName ;
    @Setter
    private Object target ;
    private Object proxyObject ;

    @Override
    public void destroy() throws Exception {
        log.info("destroy...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        proxyObject = Proxy.newProxyInstance(
            this.getClass().getClassLoader(),
            new Class[]{Class.forName(interfaceName)},
            new MyInvocationHandler(this.target)
        ) ;
        log.info("afterPropertiesSet...");
    }

    @Override
    public Object getObject() throws Exception {
        log.info("getObject....");
        return proxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return proxyObject == null ? Object.class : proxyObject.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

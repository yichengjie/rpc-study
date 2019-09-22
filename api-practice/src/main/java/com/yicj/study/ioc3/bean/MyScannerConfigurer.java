package com.yicj.study.ioc3.bean;

import com.yicj.study.ioc3.annotation.MyBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String basePackage = "com.yicj.study.ioc3.service";
        MyClassPathDefinitionScanner scanner = new MyClassPathDefinitionScanner(registry, MyBean.class) ;
        scanner.registerTypeFilter();
        int beanCount = scanner.scan(basePackage);
        String[] beanDefinitionNames = registry.getBeanDefinitionNames();
        System.out.println();
        System.out.println("beanCount : " + beanCount);
        for (String beanDefinitionName : beanDefinitionNames){
            System.out.println("beanDefinitionName : "+ beanDefinitionName);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}

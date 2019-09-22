package com.yicj.study.ioc.bean;

import com.yicj.study.ioc.vo.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

//Spring动态生成Bean的定义-BeanDefinition
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        GenericBeanDefinition beanDefinition = getGenericBeanDefinition() ;
        DefaultListableBeanFactory defaultListableBeanFactory =
                (DefaultListableBeanFactory)beanFactory ;
        defaultListableBeanFactory.registerBeanDefinition(
                "dynamicPerson1",beanDefinition);
    }

    private GenericBeanDefinition getGenericBeanDefinition(){
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(Person.class);
        beanDefinition.getPropertyValues().add("name","张三");
        return beanDefinition ;
    }
}

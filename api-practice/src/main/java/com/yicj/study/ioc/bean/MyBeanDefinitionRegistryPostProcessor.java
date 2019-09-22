package com.yicj.study.ioc.bean;

import com.yicj.study.ioc.vo.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegistryPostProcessor
        implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(
            BeanDefinitionRegistry registry) throws BeansException {
        GenericBeanDefinition beanDefinition = getGenericBeanDefinition() ;
        registry.registerBeanDefinition("dynamicPerson2",beanDefinition);
    }

    private GenericBeanDefinition getGenericBeanDefinition(){
        GenericBeanDefinition definition = new GenericBeanDefinition() ;
        definition.setBeanClass(Person.class);
        definition.getPropertyValues().add("name","李四") ;
        return definition ;
    }

    @Override
    public void postProcessBeanFactory(
            ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}

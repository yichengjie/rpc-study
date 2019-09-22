package com.yicj.study.ioc3.bean;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

//编写扫描器
//通过自定义的扫描器,扫描指定包下所有被@MyBean 注释的类。
public class MyClassPathDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private Class<? extends Annotation> type;
    public MyClassPathDefinitionScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> type) {
        super(registry);
        this.type = type ;
    }
    /**
     * 注册 过滤器
     */
    public void registerTypeFilter(){
        addIncludeFilter(new AnnotationTypeFilter(type));
    }
}

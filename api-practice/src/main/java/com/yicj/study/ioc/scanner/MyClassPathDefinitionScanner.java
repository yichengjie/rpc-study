package com.yicj.study.ioc.scanner;


import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

public class MyClassPathDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private Class type;

    public MyClassPathDefinitionScanner(BeanDefinitionRegistry registry,Class<? extends Annotation> type) {
        super(registry,false);
        this.type = type ;
    }

    //注册过滤器
    public void registerTypeFilter(){
        addIncludeFilter(new AnnotationTypeFilter(type));
    }
}

package com.yicj.study.ioc3.bean;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

//编写扫描器
//通过自定义的扫描器,扫描指定包下所有被@MyBean 注释的类。
public class MyClassPathDefinitionScanner extends ClassPathBeanDefinitionScanner {
    private Class<? extends Annotation> type;
    public MyClassPathDefinitionScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> type) {
        super(registry);
        this.type = type ;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);
        System.out.println("\n----------------------------");
        System.out.println("definitionHolders num : " + definitionHolders.size());
        for (BeanDefinitionHolder holder : definitionHolders){
            System.out.println(holder.getBeanDefinition());
        }
        System.out.println("----------------------------");
        return definitionHolders ;
    }

    /**
     * 注册 过滤器
     */
    public void registerTypeFilter(){
        addIncludeFilter(new AnnotationTypeFilter(type));
    }
}

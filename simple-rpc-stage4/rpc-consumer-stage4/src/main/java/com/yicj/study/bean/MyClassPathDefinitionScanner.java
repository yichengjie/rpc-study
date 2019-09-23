package com.yicj.study.bean;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

//编写扫描器
//通过自定义的扫描器,扫描指定包下所有被@MyBean 注释的类。
public class MyClassPathDefinitionScanner extends ClassPathBeanDefinitionScanner {
    public MyClassPathDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);
        if(!definitionHolders.isEmpty()){////处理扫描到的接口
            this.processBeanDefinitions(definitionHolders);
        }
        for (BeanDefinitionHolder holder : definitionHolders){
            System.out.println(holder.getBeanDefinition());
        }
        return definitionHolders ;
    }

    //处理扫描到的接口
    private void processBeanDefinitions(Set<BeanDefinitionHolder> definitionHolders){
        for (BeanDefinitionHolder holder : definitionHolders){
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(ServiceFactory.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }

    /**
     * 注册 过滤器
     */
    public void registerTypeFilter(){
        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }


    //这个方法一定要重写，默认接口是不会被扫描进来的
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}

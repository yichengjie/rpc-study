package com.yicj.study.ioc2.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

public class ProxyServiceScanner extends ClassPathBeanDefinitionScanner {
    private Class<? extends Annotation> annotationClass;

    public ProxyServiceScanner(BeanDefinitionRegistry registry,
                               Class<? extends Annotation> annotationClass) {
        super(registry);
        this.annotationClass = annotationClass ;
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> definitionHolders = super.doScan(basePackages);
        //对definitionHolders处理
        if (definitionHolders.isEmpty()) {
            String ps = Arrays.toString(basePackages);
            logger.warn("No RPC mapper was found in '"+ps+ "' package.");
        } else {
            processBeanDefinitions(definitionHolders);
        }
        //返回处理完成的definitionHolders
        return definitionHolders ;
    }

    public void registerFilters(){
        if(this.annotationClass != null){//如果存在注解，则仅扫面带注解的类
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }else {//扫面路径下的全部文件
            super.addIncludeFilter( (metadataReader,metadataReaderFactory) -> true);
        }
        super.addExcludeFilter( (metadataReader,metadataReaderFactory)->{
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> definitionHolders) {
        for (BeanDefinitionHolder holder : definitionHolders) {
            GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();
            definition.getConstructorArgumentValues()
                    .addGenericArgumentValue(definition.getBeanClassName());
            definition.setBeanClass(ProxyFactoryBean.class);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }
    }
}

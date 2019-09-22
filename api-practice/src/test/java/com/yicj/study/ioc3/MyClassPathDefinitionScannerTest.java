package com.yicj.study.ioc3;

import com.yicj.study.ioc3.annotation.MyBean;
import com.yicj.study.ioc3.bean.MyClassPathDefinitionScanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MyClassPathDefinitionScannerTest {


    @Test
    public void testSimpleScanner(){
        String basePackage = "com.yicj.study.ioc3.service" ;
        GenericApplicationContext context = new GenericApplicationContext( );
        MyClassPathDefinitionScanner scanner = new MyClassPathDefinitionScanner(context, MyBean.class);
        //注册过滤器
        scanner.registerTypeFilter();
        int beanCount = scanner.scan(basePackage) ;
        context.refresh();
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println();
        System.out.println("beanCount : " + beanCount);
        for (String beanDefinitionName : beanDefinitionNames){
            System.out.println("beanDefinitionName : "+ beanDefinitionName);
        }


    }

}

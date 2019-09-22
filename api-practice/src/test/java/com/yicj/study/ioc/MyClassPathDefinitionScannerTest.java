package com.yicj.study.ioc;

import com.yicj.study.ApiPracticeApplication;
import com.yicj.study.ioc.annotation.MyBeanAnnotation;
import com.yicj.study.ioc.scanner.MyClassPathDefinitionScanner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiPracticeApplication.class)
public class MyClassPathDefinitionScannerTest {

    @Test
    public void testSimpleScan(){
        String basePackage = "com.yicj.study.ioc" ;
        GenericApplicationContext context = new GenericApplicationContext( );
        MyClassPathDefinitionScanner myScanner =
                new MyClassPathDefinitionScanner(context, MyBeanAnnotation.class) ;
        int beanCount = myScanner.scan(basePackage) ;
        System.out.println("beanCount : " + beanCount);
        context.refresh();
        String [] beanDefinitionNames = context.getBeanDefinitionNames() ;

        for(String beanDefinitionName : beanDefinitionNames){
            System.out.println("beanDefinitionName : " + beanDefinitionName);
        }
    }
}

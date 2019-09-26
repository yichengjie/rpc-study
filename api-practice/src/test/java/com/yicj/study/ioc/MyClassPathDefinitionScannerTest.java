package com.yicj.study.ioc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.yicj.study.ioc.vo.Person ;

@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ApiPracticeApplication.class)
public class MyClassPathDefinitionScannerTest {

    @Test
    public void testDynamicPerson() throws IOException, InterruptedException {
        String basePackage = "com.yicj.study.ioc" ;
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(basePackage) ;
        Map<String, Person> beansOfType = context.getBeansOfType(Person.class);
        Set<Map.Entry<String, Person>> entries = beansOfType.entrySet();
        for(Map.Entry<String, Person> entry: entries){
            String key = entry.getKey();
            Person value = entry.getValue();
            System.out.println("key : " + key +" , value : " + value);
        }
        //context.close();
        System.in.read() ;
    }
}

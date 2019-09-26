package com.yicj.study.bean1;

import com.yicj.study.bean1.service.IHelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import com.yicj.study.bean1.BeanApplication ;
import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeanApplication.class)
public class BeanApplicationTest {

    @Resource(name = "helloService")
    private IHelloService helloService ;
    @Resource(name = "fbHelloWorldService")
    private IHelloService fbHelloWorldService ;
    @Resource(name = "&fbHelloWorldService")
    private Object object ;
    
    @Autowired
    private ApplicationContext context;   

    @Test
    public void testMyFactoryBean(){
        String info = helloService.sayHello("yicj");
        System.out.println("info : " + info);
        //Object bean = context.getBean("&fbHelloWorldService");
        System.out.println("obj : " + object.getClass().getName());
    }
}

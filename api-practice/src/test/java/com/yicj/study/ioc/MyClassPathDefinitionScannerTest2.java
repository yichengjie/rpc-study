package com.yicj.study.ioc;

import com.yicj.study.ApiPracticeApplication;
import com.yicj.study.ioc.vo.Person;
import com.yicj.study.ioc2.service.IUserService;
import com.yicj.study.ioc2.vo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiPracticeApplication.class)
public class MyClassPathDefinitionScannerTest2 {
    //@Autowired
    //private IUserService userService ;

    @Test
    public void testDynamicPerson() throws IOException, InterruptedException {
        //System.out.println(userService.getClass().getName());
        System.out.println("hello world");
    }
}

package com.yicj.study.ioc;

import com.yicj.study.ioc2.Ioc2Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Ioc2Application.class)
public class MyClassPathDefinitionScannerTest2 {
    //@Autowired
    //private IUserService userService ;

    @Test
    public void testDynamicPerson() throws IOException, InterruptedException {
        //System.out.println(userService.getClass().getName());
        System.out.println("hello world");
    }
}

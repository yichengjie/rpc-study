package com.yicj.study.ioc2;

import com.yicj.study.ioc2.service.IUserService;
import com.yicj.study.ioc2.vo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Ioc2Application.class)
public class MyClassPathDefinitionScannerTest2 {
    @Autowired
    private IUserService userService ;

    @Test
    public void testDynamicPerson() throws IOException, InterruptedException {
        System.out.println("hello world");
        User user = new User("yicj") ;
        List<User> users = userService.insertUser(user);
        System.out.println(users);
    }
}

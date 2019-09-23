package com.yicj.study.ioc4;

import com.yicj.study.ioc4.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Ioc4Application.class)
public class MyClassPathDefinitionScannerTest {

    @Autowired
    private IUserService userService ;
    @Test
    public void testSimpleScanner()  {
        String retStr = this.userService.hello("yicj ", "henan");
        System.out.println("====> " + retStr);
    }

}

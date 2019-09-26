package com.yicj.study.ioc5;

import com.yicj.study.ioc5.service.ICalculateService;
import com.yicj.study.ioc5.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.yicj.study.ioc5.Ioc5Application ;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Ioc5Application.class)
public class MyClassPathDefinitionScannerTest {
    @Autowired
    private IUserService userService ;
    @Autowired
    private ICalculateService calculateService ;

    @Test
    public void testSimpleScanner()  {
        String helloStr = userService.hello("yicj");
        String calculateResult = calculateService.getResult("测试");
        System.out.println("helloStr : " + helloStr);
        System.out.println("calculateResult : " + calculateResult);
    }

}

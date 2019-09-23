package com.yicj.study.ioc4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Ioc4Application.class)
public class MyClassPathDefinitionScannerTest {
    @Test
    public void testSimpleScanner()  {
        System.out.println("hello world");
    }

}

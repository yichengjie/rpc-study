package com.yicj.study.bean1.config;

import com.yicj.study.bean1.common.MyFactoryBean;
import com.yicj.study.bean1.service.IHelloService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class HelloConf {
    @Resource(name = "helloService")
    private IHelloService helloService ;

    @Bean
    public MyFactoryBean fbHelloWorldService(){
        MyFactoryBean myFactoryBean = new MyFactoryBean() ;
        myFactoryBean.setInterfaceName(IHelloService.class.getName());
        myFactoryBean.setTarget(helloService);
        return myFactoryBean ;
    }
}

package com.yicj.study.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yicj.study.vo.InfoUser;

@Controller
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    @ResponseBody
    public InfoUser index(){
        InfoUser user = new InfoUser(UUID.randomUUID().toString(),"王思萌","BeiJing");
        logger.info(user.toString());
        return user;
    }
}
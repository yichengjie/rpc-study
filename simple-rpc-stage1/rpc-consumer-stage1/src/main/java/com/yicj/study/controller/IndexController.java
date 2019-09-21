package com.yicj.study.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.yicj.study.service.InfoUserService;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.InfoUser;
import com.yicj.study.vo.Response;
//https://www.jianshu.com/p/c5ecd4de6fd1
@Controller
public class IndexController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    InfoUserService userService;

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return  new Date().toString();
    }

    @RequestMapping("insert")
    @ResponseBody
    public List<InfoUser> insert() throws InterruptedException {
        long start = System.currentTimeMillis();
        int thread_count = 5;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                InfoUser infoUser = new InfoUser(IdUtil.getId(),"Jeen","BeiJing");
                List<InfoUser> users = userService.insertInfoUser(infoUser);
                logger.info("返回用户信息记录:{}", users);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));
        return null;
    }
    
    
    @RequestMapping("insertAsync")
    @ResponseBody
    public List<InfoUser> insertAsync() throws Exception {
        long start = System.currentTimeMillis();
        InfoUser infoUser = new InfoUser(IdUtil.getId(),"Jeen","BeiJing");
        Future<Response> futrue = userService.insertInfoUserAsync(infoUser);
        Response resp = futrue.get() ;
        Object users = resp.getData();
        logger.info("返回用户信息记录:{}", users);
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",1,(end-start));
        return null;
    }

    @RequestMapping("getById")
    @ResponseBody
    public InfoUser getById(String id){
        logger.info("根据ID查询用户信息:{}",id);
        return userService.getInfoUserById(id);
    }

    @RequestMapping("getNameById")
    @ResponseBody
    public String getNameById(String id){
        logger.info("根据ID查询用户名称:{}",id);
        return userService.getNameById(id);
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public Map<String,InfoUser> getAllUser() throws InterruptedException {

        long start = System.currentTimeMillis();
        int thread_count = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                Map<String, InfoUser> allUser = userService.getAllUser();
                logger.info("查询所有用户信息：{}",allUser);
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));

        return null;
    }
}

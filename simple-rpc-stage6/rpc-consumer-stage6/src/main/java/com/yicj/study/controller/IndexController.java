package com.yicj.study.controller;

import com.yicj.study.service.client.IUserService;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.Future;

@RestController
public class IndexController {
    @Autowired
    private IUserService userService ;

    @GetMapping("/index")
    public String index(){
        return "hello world" ;
    }

    @GetMapping("/insertUser")
    public List<User> insertUser() throws InterruptedException {
        String id = IdUtil.getId() ;
        String name = "yicj" ;
        String address = "henan" ;
        User user = new User(id,name,address) ;
        List<User> users = userService.insertUser(user);
        return users ;
    }
    
    @GetMapping("/insertUserAsync")
    public List<User> insertUserAsync() throws Exception  {
        String id = IdUtil.getId() ;
        String name = "yicj-async" ;
        String address = "henan-async" ;
        User user = new User(id,name,address) ;
        Future<List<User>> future = userService.insertUserAsync(user);
        List<User> users = future.get();
        return users ;
    }
}

package com.yicj.study.controller;

import com.yicj.study.rpc.ConnectManage;
import com.yicj.study.rpc.RpcFactory;
import com.yicj.study.service.IUserService;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import com.yicj.study.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class IndexController {
    @Autowired
    private RpcFactory rpcFactory ;

    @GetMapping("/index")
    public String index(){
        return "hello world" ;
    }

    @GetMapping("/insertUser")
    public List<User> insertUser() throws InterruptedException {
        IUserService userService = rpcFactory.getServiceImpl(IUserService.class) ;
        String id = IdUtil.getId() ;
        String name = "yicj" ;
        String address = "henan" ;
        User user = new User(id,name,address) ;
        List<User> users = userService.insertUser(user);
        return users ;
    }
}

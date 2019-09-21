package com.yicj.study.controller;

import com.yicj.study.rpc.ConnectManage;
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
    private ConnectManage connectManage ;


    @GetMapping("/index")
    public String index(){
        this.connectManage.init();
        return "hello world" ;
    }

    @GetMapping("/insertUser")
    public List<User> insertUser() throws InterruptedException {
        Request req = new Request() ;
        req.setId(IdUtil.getId());
        User user = new User(IdUtil.getId(),"yicj2","beijing2") ;
        req.setClassName(IUserService.class.getName());
        req.setMethodName("insertUser");
        req.setParameterTypes(new Class<?>[]{User.class});
        req.setParameters(new Object[]{user});
        Response response = connectManage.sendRequest(req);
        log.info("=====> " + response.toString());
        if(Response.SUCCESS.equals(response.getCode())){
            return (List<User>)response.getData() ;
        }
        return null ;
    }
}

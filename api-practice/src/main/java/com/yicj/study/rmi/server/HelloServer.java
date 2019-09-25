package com.yicj.study.rmi.server;

import com.yicj.study.rmi.common.IHello;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class HelloServer {

    public static void main(String[] args) throws Exception {
        IHello rhello = new HelloImpl() ;
        LocateRegistry.createRegistry(8888) ;
        //如果配置在远程服务器，把地址换成你的ip
        System.setProperty("java.rmi.server.hostname","127.0.0.1");
        Naming.bind("rmi://localhost:8888/RHello",rhello);
        System.out.println(">>>>>> INFO: 远程IHello对象绑定成功!");
    }

}

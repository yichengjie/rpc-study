package com.yicj.study.rmi.client;

import com.yicj.study.rmi.common.IHello;
import java.rmi.Naming;

public class Client {

    public static void main(String[] args) throws Exception{
        IHello rhello = (IHello) Naming.lookup("rmi://127.0.0.1:8888/RHello") ;
        System.out.println(rhello.sayHello("yicj"));
    }
}

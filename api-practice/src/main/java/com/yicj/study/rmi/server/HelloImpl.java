package com.yicj.study.rmi.server;

import com.yicj.study.rmi.common.IHello;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements IHello {
    protected HelloImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(String username) throws RemoteException {
        System.out.println("Connected successfully!");
        return "你好, " + username +"!";
    }
}

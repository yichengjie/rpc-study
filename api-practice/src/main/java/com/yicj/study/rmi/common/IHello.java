package com.yicj.study.rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IHello extends Remote {
    String sayHello(String username) throws RemoteException;
}

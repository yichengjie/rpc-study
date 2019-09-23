package com.yicj.study.rpc;

import com.sun.xml.internal.bind.v2.model.core.ID;
import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@Component
public class RpcFactory implements InvocationHandler {
    @Autowired
    private ConnectManage connectManage ;

    public <T> T  getServiceImpl(Class<?> clazz){
        ClassLoader loader = clazz.getClassLoader() ;
        Class<?>[] interfaces = new Class[] { clazz };
        InvocationHandler h = this ;
        return (T)Proxy.newProxyInstance(loader, interfaces, h) ;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] interfaces = proxy.getClass().getInterfaces();
        String id = IdUtil.getId() ;
        String className = interfaces[0].getName() ;
        String methodName = method.getName() ;
        Class<?> [] parameterTypes = method.getParameterTypes() ;
        Object [] parameters = args ;
        Request request = new Request(id,className,methodName,parameterTypes,parameters) ;
        Response response = connectManage.sendRequest(request);
        return response.getData();
    }
}

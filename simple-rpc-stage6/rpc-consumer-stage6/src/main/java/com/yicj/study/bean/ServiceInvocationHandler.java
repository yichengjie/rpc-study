package com.yicj.study.bean;

import com.yicj.study.rpc.ConnectManage;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//动态代理，需要注意的是，这里用到的是JDK自带的动态代理，代理对象只能是接口，不能是类
@Component
public class ServiceInvocationHandler implements InvocationHandler {
    @Autowired
    private ConnectManage connectManage ;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?>[] interfaces = proxy.getClass().getInterfaces();
        String id = IdUtil.getId() ;
        String className = interfaces[0].getName() ;
        String methodName = method.getName() ;
        className = className.replace("service.client", "service");
        //如果是异步接口调用，则返回Feature
        Class<?> [] parameterTypes = method.getParameterTypes() ;
        Object [] parameters = args ;
        boolean asyncFlag = false;
        if(methodName.endsWith("Async")) {
           methodName = methodName.substring(0, methodName.length() -5) ;
           asyncFlag = true ;
        }
    	Request request = new Request(id,className,methodName,parameterTypes,parameters) ;
    	if(asyncFlag) {
    		return connectManage.sendRequestAsync(request);
    	}else {
    		Response response = connectManage.sendRequest(request);
        	return response.getData();
    	}
    }
}

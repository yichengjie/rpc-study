package com.yicj.study.ioc2.bean;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Component
public class MyInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> [] clazzList = proxy.getClass().getInterfaces() ;
        String methodName = method.getName() ;
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        StringBuilder builder = new StringBuilder() ;
        builder.append("--------方法准备执行---------\n");
        builder.append("类名称: " + clazzList[0].getClasses()+"\n");
        builder.append("方法名称: " + methodName +"\n");
        builder.append("方法参数类型: ");
        for(Class<?> pt : parameterTypes){
            builder.append(pt.getSimpleName() +", ");
        }
        builder.append("\n");
        builder.append("方法返回值类型: " + returnType.getName()+"\n");
        builder.append("--------方法执行完成---------\n" );
        System.out.println(builder.toString());
        return null ;
    }
}

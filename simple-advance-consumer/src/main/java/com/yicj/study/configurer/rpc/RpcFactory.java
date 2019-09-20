package com.yicj.study.configurer.rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yicj.study.netty.client.NettyClient;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;

@Component
public class RpcFactory<T> implements InvocationHandler {

    @Autowired
    private NettyClient client;
    //private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        String methodName = method.getName() ;
        request.setClassName(method.getDeclaringClass().getName());
        if(method.getName().endsWith("Async")) {
        	methodName = methodName.substring(0, methodName.length() -5) ;
        }
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setParameterTypes(method.getParameterTypes());
        request.setId(IdUtil.getId());
        Response resp = client.send(request);
        return resp.getData() ;
        /*Class<?> returnType = method.getReturnType();
        Response response = JSON.parseObject(result.toString(), Response.class);
        if (response.getCode()==1){
            throw new Exception(response.getErrorMsg());
        }
        if (returnType.isPrimitive() || String.class.isAssignableFrom(returnType)){
            return response.getData();
        }else if (Collection.class.isAssignableFrom(returnType)){
            return JSONArray.parseArray(response.getData().toString(),Object.class);
        }else if(Map.class.isAssignableFrom(returnType)){
            return JSON.parseObject(response.getData().toString(),Map.class);
        }else{
            Object data = response.getData();
            return JSONObject.parseObject(data.toString(), returnType);
        }*/
    }
}
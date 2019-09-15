package com.yicj.study.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.yicj.study.common.RpcRequestVo;
import com.yicj.study.common.RpcResponseVo;
import com.yicj.study.rpc.RpcReqClient;
import com.yicj.study.util.IdUtil;

public class ProxyHandler implements InvocationHandler {
	private RpcReqClient rpcReqClient ;
	
	public ProxyHandler(RpcReqClient rpcReqClient) {
		this.rpcReqClient = rpcReqClient ;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getService(Class <?> clazz) {
		return (T)Proxy.newProxyInstance(
				clazz.getClassLoader(), 
				new Class<?>[] {clazz}, 
				this) ;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String interfaceName = proxy.getClass().getInterfaces()[0].getName();
		String methodName = method.getName();
		Class<?>[] methodParameterTypes = method.getParameterTypes() ;
		Object[] methodParameters = args ;
		RpcRequestVo vo = new RpcRequestVo(IdUtil.getId(),interfaceName, methodName, methodParameterTypes, methodParameters) ;
		RpcResponseVo resp = rpcReqClient.sendRequest(vo);
		Object data = resp.getData();
		return data;
	}
}

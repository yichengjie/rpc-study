package com.yicj.study.rpc;

import java.lang.reflect.Proxy;

/**
 * 1. 封装一个代理类处理器 
 * 2. 返回service的代理类对象
 * @author yicj
 */
public class RpcConsumer {
	public static <T> T getService(Class<T> clazz, String ip, int port) {
		ProxyHandler proxyHandler = new ProxyHandler(ip, port);
		return (T) Proxy.newProxyInstance(RpcConsumer.class.getClassLoader(), new Class<?>[] { clazz }, proxyHandler);
	}
}

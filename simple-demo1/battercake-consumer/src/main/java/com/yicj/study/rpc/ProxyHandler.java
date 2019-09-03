package com.yicj.study.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
/**
 *1 建立socket连接
 *2 封装请求数据，发送给服务提供者
 *3 返回结果
 * @author yicj
 */
public class ProxyHandler implements InvocationHandler {
	private String ip;
	private int port;

	public ProxyHandler(String ip, int port) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = port;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Socket socket = new Socket(this.ip, this.port);
		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		try {
			output.writeObject(proxy.getClass().getInterfaces()[0]);
			output.writeUTF(method.getName());
			output.writeObject(method.getParameterTypes());
			output.writeObject(args);
			output.flush();
			Object result = input.readObject();
			if (result instanceof Throwable) {
				throw (Throwable) result;
			}
			return result;
		} finally {
			socket.shutdownOutput();
			socket.close();
		}
	}

}

package com.yicj.study.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;

/**
 * 1.读取客户端发送的服务名 2.判断服务是否发布 3. 如果发布，则走反射逻辑，动态调用，返回结果 4.如果未发布，则返回提示通知
 */
public class ServerThread implements Runnable {
	private Socket client = null;
	private List<Object> serviceList = null;
	public ServerThread(Socket client, List<Object> service) {
		this.client = client;
		this.serviceList = service;
	}

	@Override
	public void run() {
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		try {
			input = new ObjectInputStream(client.getInputStream());
			output = new ObjectOutputStream(client.getOutputStream());
			// 读取客户端要访问那个service
			Class<?> serviceClass = (Class<?>) input.readObject();
			// 找到该服务类
			Object obj = findService(serviceClass);
			if (obj == null) {
				output.writeObject(serviceClass.getName() + "服务未发现");
			} else {
				// 利用反射调用该方法，返回结果
				try {
					String methodName = input.readUTF();
					Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
					Object[] arguments = (Object[]) input.readObject();
					Method method = obj.getClass().getMethod(methodName, parameterTypes);
					Object result = method.invoke(obj, arguments);
					output.writeObject(result);
				} catch (Throwable t) {
					output.writeObject(t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
				input.close();
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Object findService(Class<?> serviceClass) {
		for (Object obj : serviceList) {
			boolean isFather = serviceClass.isAssignableFrom(obj.getClass());
			if (isFather) {
				return obj;
			}
		}
		return null;
	}
}

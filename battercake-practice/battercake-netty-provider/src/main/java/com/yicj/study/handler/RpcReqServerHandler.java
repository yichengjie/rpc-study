package com.yicj.study.handler;


import java.lang.reflect.Method;
import java.util.List;

import com.yicj.study.common.RpcRequestVo;
import com.yicj.study.common.RpcResponseVo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcReqServerHandler extends ChannelInboundHandlerAdapter {
	private List<Object> serviceList ;
	
	public RpcReqServerHandler(List<Object> serviceList) {
		this.serviceList = serviceList ;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcRequestVo req = (RpcRequestVo) msg ;
		//log.info("SubReqServerHandler  channelRead method is call ...");
		//经过解码器handler ObjectDecoder的解码
		//SubReqServerHandler接收到的请求消息已经被自动解码为SubscribeReq对象，可以直接使用
		//if("yicj".equalsIgnoreCase(req.getUserName())) {
		log.info("server accept client req:["+msg.toString()+"]");
		//对订购者的用户名进行合法性验证，校验通过后打印订购请求信息，
		//构造订购成功应答消息立即发送给客户端
		ctx.writeAndFlush(resp(req)) ;
		//}
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
	
	private RpcResponseVo resp(RpcRequestVo req) throws ClassNotFoundException {
		String interfaceName = req.getInterfaceName();
		String methodName = req.getMethodName() ;
		Class<?> [] methodParameterTypes = req.getMethodParameterTypes();
		Object[] methodParameters = req.getMethodParameters();
		Class<?> serviceClass = Class.forName(interfaceName);
		RpcResponseVo resp = new RpcResponseVo("404", interfaceName+"服务未发现", null) ;
		Object service = findService(serviceClass);
		if (service != null) {
			try {
				Method method = service.getClass().getMethod(methodName, methodParameterTypes);
				Object result = method.invoke(service, methodParameters);
				resp = new RpcResponseVo("200", "success", result) ;
			} catch (Throwable t) {
				resp = new RpcResponseVo("500", "执行方法报错", t) ;
			}
		} 
		return resp ;
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错 : " ,cause);
		ctx.close() ;
	}
}

package com.yicj.study.handler;

import com.yicj.study.rpc.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
@Component
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
	private Map<String, Object> serviceMap = new HashMap<>();


	public NettyServerHandler(Map<String, Object> serviceMap){
		this.serviceMap = serviceMap ;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("客户端连接成功!" + ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.info("客户端断开连接!{}", ctx.channel().remoteAddress());
		ctx.channel().close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		Request request = (Request) msg;
		if ("heartBeat".equals(request.getMethodName())) {
			logger.info("客户端心跳信息..." + ctx.channel().remoteAddress());
		} else {
			String className = request.getClassName() ;
			logger.info("RPC客户端请求接口:" + request.getClassName() + "   方法名:" + request.getMethodName());
			Object service = this.serviceMap.get(className);
			if(service != null){
				Response response = this.doExecuteMethod(request, service);
				ctx.writeAndFlush(response);
			}else{
				logger.warn("服务{}未注册,请检查服务是否正确!",className);
				Response response = new Response();
				response.setRequestId(request.getId());
				response.setCode(Response.ERROR);
				response.setErrorMsg("服务未注册,请检查服务是否正确!");
				response.setData("服务未注册,请检查服务是否正确!");
				ctx.writeAndFlush(response);
			}
		}
	}

	private Response doExecuteMethod(Request request,Object service){
		Response response = new Response();
		response.setRequestId(request.getId());
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		Method method = null ;
		try {
			method = service.getClass().getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			Object retObj = method.invoke(service, parameters);
			response.setData(retObj);
		}catch (NoSuchMethodException e){
			logger.error("方法"+methodName+"不存在,请检查!",e);
			response.setCode(Response.ERROR);
			response.setErrorMsg("方法"+methodName+"不存在,请检查!");
		} catch (IllegalAccessException e) {
			response.setCode(Response.ERROR);
			response.setErrorMsg("方法"+methodName+"为非公有方法无法调用!");
		} catch (InvocationTargetException e) {
			response.setCode(Response.ERROR);
			response.setErrorMsg("方法"+methodName+"执行出错!"+e.getMessage());
		}
		return response ;
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE) {
				logger.info("客户端已超过60秒未读写数据,关闭连接.{}", ctx.channel().remoteAddress());
				ctx.channel().close();
			}
		} else {
			logger.info("其他事件....");
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("====> 服务端发生异常", cause);
		ctx.close();
	}

}
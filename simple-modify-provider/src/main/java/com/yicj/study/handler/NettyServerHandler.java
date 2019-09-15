package com.yicj.study.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

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
			logger.info("RPC客户端请求接口:" + request.getClassName() + "   方法名:" + request.getMethodName());
			Response response = new Response();
			response.setRequestId(request.getId());
			try {
				response.setData("hello world");
			} catch (Throwable e) {
				response.setCode(1);
				response.setErrorMsg(e.toString());
				logger.error("RPC Server handle request error", e);
			}
			ctx.writeAndFlush(response);
		}
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
		logger.info(cause.getMessage());
		ctx.close();
	}

}
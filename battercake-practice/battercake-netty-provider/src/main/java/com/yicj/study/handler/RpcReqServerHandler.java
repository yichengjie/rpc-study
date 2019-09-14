package com.yicj.study.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcReqServerHandler extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//log.info("SubReqServerHandler  channelRead method is call ...");
		//经过解码器handler ObjectDecoder的解码
		//SubReqServerHandler接收到的请求消息已经被自动解码为SubscribeReq对象，可以直接使用
		//if("yicj".equalsIgnoreCase(req.getUserName())) {
		log.info("Service accept client subscribe req:["+msg.toString()+"]");
		//对订购者的用户名进行合法性验证，校验通过后打印订购请求信息，
		//构造订购成功应答消息立即发送给客户端
		//ctx.writeAndFlush(resp(req.getSubReqID())) ;
		//}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错 : " ,cause);
		ctx.close() ;
	}
}

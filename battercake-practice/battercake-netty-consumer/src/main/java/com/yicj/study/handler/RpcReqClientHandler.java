package com.yicj.study.handler;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

import com.yicj.study.common.RpcRequestVo;
import com.yicj.study.common.RpcResponseVo;
import com.yicj.study.util.IdUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcReqClientHandler extends ChannelInboundHandlerAdapter{
	private ConcurrentHashMap<String,SynchronousQueue<RpcResponseVo>> queueMap = new ConcurrentHashMap<>();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//在链路激活的时候循环构造10条订购消息，最后一次性地发送给服务器端
		String interfaceName = "com.yicj.study.service.BatterCakeService" ;
		String methodName = "sellBatterCake" ;
		Class<?>[] methodParameterTypes = new Class [] {String.class} ;
		Object[] methodParameters = new Object[] {"hello"} ;
		RpcRequestVo vo = new RpcRequestVo(IdUtil.getId(),interfaceName, methodName, methodParameterTypes, methodParameters) ;
		ctx.writeAndFlush(vo) ;
	}
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		RpcResponseVo resp = (RpcResponseVo) msg ;
		//由于对象解码器已经对订购请求应答消息进行了自动解码
		//因此，SubReqClientHandler接收到的消息已经是解码成功后的订购应答消息
		log.info("Receive server response :["+msg.toString()+"]");
		SynchronousQueue<RpcResponseVo> queue = this.queueMap.get(resp.getReqId());
		if(queue!=null) {
			queue.put(resp); ;
			this.queueMap.remove(resp.getReqId()) ;
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush() ;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("出错:" ,cause);
		ctx.close() ;
	}
	
	
	public SynchronousQueue<RpcResponseVo> sendRequest(RpcRequestVo req, Channel channel) {
		SynchronousQueue<RpcResponseVo> queue = new SynchronousQueue<RpcResponseVo>();
		queueMap.put(req.getReqId(), queue) ;
		channel.writeAndFlush(req) ;
		return queue ;
	}
	
}

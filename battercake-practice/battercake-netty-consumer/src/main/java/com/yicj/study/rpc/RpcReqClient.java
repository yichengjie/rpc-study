package com.yicj.study.rpc;

import java.util.concurrent.SynchronousQueue;

import com.yicj.study.common.MarshallingCodeCFactory;
import com.yicj.study.common.RpcRequestVo;
import com.yicj.study.common.RpcResponseVo;
import com.yicj.study.handler.RpcReqClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcReqClient {
	
	private Channel channel ;
	private RpcReqClientHandler rpcReqClientHandler ;

	public RpcReqClient connect(String host,int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup() ;
		rpcReqClientHandler = new RpcReqClientHandler() ;
		try {
			Bootstrap boot = new Bootstrap() ;
			boot.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
					ch.pipeline().addLast(rpcReqClientHandler) ;
				}
				
			}); 
			//发起异步连接操作
			ChannelFuture f = boot.connect(host,port).sync();
			Channel channel = f.channel();
			this.channel = channel ;
			//等待链路关闭
			//channel.closeFuture().sync() ;
		} finally {
			//group.shutdownGracefully() ;
		}
		return this ;
	}
	
	public RpcResponseVo sendRequest(RpcRequestVo req) throws InterruptedException {
		SynchronousQueue<RpcResponseVo> queue = this.rpcReqClientHandler.sendRequest(req, channel);
		RpcResponseVo take = queue.take();
		return take ;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080  ;
		String host = "127.0.0.1" ;
		new RpcReqClient().connect(host, port);
	}
	
}

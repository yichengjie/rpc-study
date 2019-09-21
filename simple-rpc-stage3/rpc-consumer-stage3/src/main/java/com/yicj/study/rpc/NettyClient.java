package com.yicj.study.rpc;

import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.netty.codec.MarshallingCodeCFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import javax.annotation.PreDestroy;

public class NettyClient {

	private EventLoopGroup group = new NioEventLoopGroup();

	public Channel start(String host,int port) throws InterruptedException {
		Bootstrap boot = new Bootstrap();
		boot.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline p = channel.pipeline();
				p.addLast(new IdleStateHandler(0, 0, 5));
				p.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
				p.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
				p.addLast("handler",new NettyClientHandler());
			}
		});
		ChannelFuture future = boot.connect(host,port);
		Channel channel = future.sync().channel() ;
		return channel ;
	}

	@PreDestroy
	public void destroy(){
		group.shutdownGracefully();
	}



	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1" ;
		int port = 18868 ;
		NettyClient client = new NettyClient() ;
		Channel channel = client.start(host, port);
//		//服务器同步连接断开时,这句代码才会往下执行
		channel.closeFuture().sync() ;
		client.destroy();
	}
}
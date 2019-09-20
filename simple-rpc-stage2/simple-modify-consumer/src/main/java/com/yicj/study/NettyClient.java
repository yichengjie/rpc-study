package com.yicj.study;

import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.netty.codec.MarshallingCodeCFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient {
	public void start(String host,int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline p = channel.pipeline();
					p.addLast(new IdleStateHandler(0, 0, 5));
					//pipeline.addLast(new JSONEncoder());
					//pipeline.addLast(new JSONDecoder());
					p.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					p.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
					p.addLast("handler",new NettyClientHandler());
				}
			});
			ChannelFuture future = boot.connect(host,port);
			future.channel().closeFuture().sync() ;
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1" ;
		int port = 8080 ;
		NettyClient client = new NettyClient() ;
		client.start(host, port);
	}
	
	
}
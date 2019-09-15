package com.yicj.study;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.netty.codec.jboss.MarshallingCodeCFactory;
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
	public void start(SocketAddress address) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		NettyClientHandler clientHandler = new NettyClientHandler();
		bootstrap.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			// 创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast(new IdleStateHandler(0, 0, 5,TimeUnit.SECONDS));
				//pipeline.addLast(new JSONEncoder());
				//pipeline.addLast(new JSONDecoder());
				pipeline.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
				pipeline.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
				pipeline.addLast(clientHandler);
			}
		});
		ChannelFuture future = bootstrap.connect(address);
		future.channel().closeFuture().sync() ;
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1" ;
		//int port = 18868 ;
		int port = 8080 ;
		NettyClient client = new NettyClient() ;
		SocketAddress address = new InetSocketAddress(host, port) ;
		client.start(address);
	}
	
	
}
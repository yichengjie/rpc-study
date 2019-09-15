package com.yicj.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.handler.NettyServerHandler;
import com.yicj.study.netty.codec.jboss.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServer {
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
	
	//private static final EventLoopGroup workerGroup = new NioEventLoopGroup(4);
	public void init() throws Exception {
		int port = 8080 ; 
		start(port);
	}
	public void start(int port) {
	    NettyServerHandler handler = new NettyServerHandler();
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(group)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				// 创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast(new IdleStateHandler(0, 0, 60));
					//pipeline.addLast(new JSONEncoder());
					//pipeline.addLast(new JSONDecoder());
					pipeline.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					pipeline.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
					pipeline.addLast(handler);
				}
			});
			ChannelFuture cf = bootstrap.bind(port).sync();
			logger.info("RPC 服务器启动.监听端口:" + port);
			// 等待服务端监听端口关闭
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		NettyServer server = new NettyServer() ;
		server.init();
	}

}
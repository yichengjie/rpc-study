package com.yicj.study.rpc;

import com.yicj.study.annotation.RpcServcie;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.handler.NettyServerHandler;
import com.yicj.study.netty.codec.MarshallingCodeCFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Component
public class NettyServer  implements ApplicationContextAware , InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
	@Getter
	private Map<String, Object> serviceMap = new HashMap<>();
	@Value("${rpc.server.address}")
	private String serverAddress;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcServcie.class) ;
		Collection<Object> values = beans.values();
		for(Object object : values){
			Class<?>[] interfaces = object.getClass().getInterfaces();
			for(Class<?> inter : interfaces){
				String interName = inter.getName();
				serviceMap.put(interName,object) ;
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String [] infos = serverAddress.split(":");
		Integer port = Integer.parseInt(infos[1]) ;
		start(port);
	}

	public void start(int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(ChannelOption.TCP_NODELAY, true)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				// 创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					//pipeline.addLast(new IdleStateHandler(0, 0, 20));
					pipeline.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
					pipeline.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
					pipeline.addLast(new NettyServerHandler(serviceMap));
				}
			});
			ChannelFuture cf = boot.bind(port).sync();
			logger.info("RPC 服务器启动.监听端口:" + port);
			// 等待服务端监听端口关闭
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			logger.error("启动netty服务器出错:",e);
			group.shutdownGracefully();
		}
	}
	
}
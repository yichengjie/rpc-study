package com.yicj.study.rpc;

import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.netty.codec.MarshallingCodeCFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;

@Component
public class NettyClient {
	private EventLoopGroup group = new NioEventLoopGroup();
	private Bootstrap boot = new Bootstrap();
	@Autowired
	private NettyClientHandler nettyClientHandler ;

	public NettyClient(){
		//this.start();
	}

	public void start() {
		boot.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline p = channel.pipeline();
				//p.addLast(new IdleStateHandler(0, 0, 5));
				p.addLast(MarshallingCodeCFactory.buildMarshallingEncoder()) ;
				p.addLast(MarshallingCodeCFactory.buildMarshallingDecoder()) ;
				p.addLast("handler",nettyClientHandler);
			}
		});
	}

	public Channel doConnect(String host, int port) {
		try {
			ChannelFuture future = boot.connect(host,port);
			Channel channel = future.sync().channel() ;
			return channel ;
		}catch (InterruptedException e){
			throw new RuntimeException(e) ;
		}
	}

	@PreDestroy
	public void destroy(){
		group.shutdownGracefully();
	}

	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1" ;
		int port = 18868 ;
		NettyClient client = new NettyClient() ;
		Channel channel = client.doConnect(host, port);
//		//服务器同步连接断开时,这句代码才会往下执行
		channel.closeFuture().sync() ;
		client.destroy();
	}
}
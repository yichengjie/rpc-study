package com.yicj.study;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

@Component
public class NettyClient {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private EventLoopGroup group = new NioEventLoopGroup(1);
	private Bootstrap bootstrap = new Bootstrap();
//	@Autowired
	private NettyClientHandler clientHandler = new NettyClientHandler();
//	@Autowired
//	private ConnectManage connectManage;

	public NettyClient() {
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
				pipeline.addLast("handler", clientHandler);
			}
		});
	}

	@PreDestroy
	public void destroy() {
		logger.info("RPC客户端退出,释放资源!");
		group.shutdownGracefully();
	}

//	public Object send(Request request) throws InterruptedException {
//		Channel channel = connectManage.chooseChannel();
//		if (channel != null && channel.isActive()) {
//			SynchronousQueue<Object> queue = clientHandler.sendRequest(request, channel);
//			Object result = queue.take();
//			return JSONArray.toJSONString(result);
//		} else {
//			Response res = new Response();
//			res.setCode(1);
//			res.setErrorMsg("未正确连接到服务器.请检查相关配置信息!");
//			return JSONArray.toJSONString(res);
//		}
//	}

	public void doConnect(SocketAddress address) throws InterruptedException {
		ChannelFuture future = bootstrap.connect(address);
		future.channel().closeFuture().sync() ;
	}
	
	public static void main(String[] args) throws InterruptedException {
		String host = "127.0.0.1" ;
		int port = 18868 ;
		NettyClient client = new NettyClient() ;
		SocketAddress address = new InetSocketAddress(host, port) ;
		client.doConnect(address);
		client.destroy(); 
	}
	
	
}
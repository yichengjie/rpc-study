package com.yicj.study.netty.client;

import java.net.SocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yicj.study.connection.ConnectManage;
import com.yicj.study.netty.codec.jboss.MarshallingCodeCFactory;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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
	@Autowired
	private NettyClientHandler clientHandler;
	@Autowired
	private ConnectManage connectManage;

	public NettyClient() {
		bootstrap.group(group)
		.channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.option(ChannelOption.SO_KEEPALIVE, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			// 创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast(new IdleStateHandler(0, 0, 30,TimeUnit.SECONDS));
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

	public Response send(Request request) throws InterruptedException {
		Channel channel = connectManage.chooseChannel();
		if (channel != null && channel.isActive()) {
			SynchronousQueue<Response> queue = clientHandler.sendRequest(request, channel);
			Response result = queue.take();
			//return JSONArray.toJSONString(result);
			return result ;
		} else {
			Response res = new Response();
			res.setCode(1);
			res.setErrorMsg("未正确连接到服务器.请检查相关配置信息!");
			//return JSONArray.toJSONString(res);
			return res ;
		}
	}

	public Channel doConnect(SocketAddress address) throws InterruptedException {
		ChannelFuture future = bootstrap.connect(address);
		Channel channel = future.sync().channel();
		return channel;
	}
}
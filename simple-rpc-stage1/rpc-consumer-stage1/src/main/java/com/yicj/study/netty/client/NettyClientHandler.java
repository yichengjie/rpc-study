package com.yicj.study.netty.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yicj.study.connection.ConnectManage;
import com.yicj.study.util.IdUtil;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private NettyClient client;
    @Autowired
    private ConnectManage connectManage;

    private ConcurrentHashMap<String,SynchronousQueue<Response>> queueMap = new ConcurrentHashMap<>();

    public void channelActive(ChannelHandlerContext ctx)   {
        logger.info("已连接到RPC服务器.{}",ctx.channel().remoteAddress());
        Request req = new Request() ;
        req.setId(IdUtil.getId());
        req.setMethodName("heartBeat"); 
        ctx.channel().writeAndFlush(req) ;
    }

    public void channelInactive(ChannelHandlerContext ctx)   {
        InetSocketAddress address =(InetSocketAddress) ctx.channel().remoteAddress();
        logger.info("与RPC服务器断开连接."+address);
        ctx.channel().close();
        connectManage.removeChannel(ctx.channel());
    }
    
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
        //Response response = JSON.parseObject(msg.toString(),Response.class);
    	Response response = (Response) msg ;
        String requestId = response.getRequestId();
        SynchronousQueue<Response> queue = queueMap.get(requestId);
        queue.put(response);
        queueMap.remove(requestId);
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        logger.info("RPC通信服务器发生异常.{}",cause);
        ctx.channel().close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)throws Exception {
        logger.info("已超过30秒未与RPC服务器进行读写操作!将发送心跳消息...");
        if (evt instanceof IdleStateEvent){
        	logger.info("准备发送心跳数据.....");
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state()== IdleState.ALL_IDLE){
                Request request = new Request();
                request.setMethodName("heartBeat");
                ctx.channel().writeAndFlush(request);
            }
        }else{
        	logger.info("应用数据发送....");
            super.userEventTriggered(ctx,evt);
        }
    }
    
    public SynchronousQueue<Response> sendRequest(Request request,Channel channel) {
        SynchronousQueue<Response> queue = new SynchronousQueue<>();
        queueMap.put(request.getId(), queue);
        channel.writeAndFlush(request);
        return queue;
    }

    
}
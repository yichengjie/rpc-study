package com.yicj.study.handler;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;


@Sharable
@Component
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ConcurrentHashMap<String, SynchronousQueue<Response>> queueMap = new ConcurrentHashMap<>();

    public void channelActive(ChannelHandlerContext ctx)   {
        logger.info("已连接到RPC服务器.{}",ctx.channel().remoteAddress());
        ctx.fireChannelActive() ;
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
    	Response resp = (Response) msg ;
        logger.info("client recive from server : " + resp.toString());
        String requestId = resp.getRequestId();
        SynchronousQueue<Response> queue = this.queueMap.get(requestId);
        if(queue!=null){
            queue.put(resp);
            this.queueMap.remove(requestId) ;
        }
        ctx.fireChannelRead(msg) ;
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
                Request request = Request.getHeartBeatRequest() ;
                ctx.channel().writeAndFlush(request);
            }
        }else{
        	logger.info("应用数据发送....");
            super.userEventTriggered(ctx,evt);
        }

    }

    //发送请求
    public SynchronousQueue<Response> sendRequest(Request request, Channel channel){
        String id = request.getId();
        SynchronousQueue<Response> queue = new SynchronousQueue<>() ;
        queueMap.put(id,queue) ;
        channel.writeAndFlush(request) ;
        return queue ;
    }
    
}
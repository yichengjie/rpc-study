package com.yicj.study.rpc;

import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

@Component
public class ConnectManage {
    @Autowired
    private NettyClient nettyClient ;
    @Autowired
    private NettyClientHandler nettyClientHandler ;
    private Map<String, Channel> channelNodes = new ConcurrentHashMap<>();



    @PostConstruct
    private void init(){
        String host = "127.0.0.1" ;
        int port = 18868 ;
        nettyClient.start();
        Channel channel = nettyClient.doConnect(host, port);
        String address = this.getAddress(host, port);
        channelNodes.put(address,channel) ;
    }

    private String getAddress(String host, int port){
        return  host + ":" + port ;
    }


    private Channel chooseChannel(){
        String host = "127.0.0.1" ;
        int port = 18868 ;
        String address = this.getAddress(host, port);
        return channelNodes.get(address) ;
    }

    public Response sendRequest(Request request) throws InterruptedException {
        Channel channel = this.chooseChannel();
        if (channel != null && channel.isActive()) {
            SynchronousQueue<Response> queue = nettyClientHandler.sendRequest(request, channel);
            Response result = queue.take();
            return result ;
        } else {
            Response res = new Response();
            res.setCode(Response.ERROR);
            res.setErrorMsg("未正确连接到服务器.请检查相关配置信息!");
            return res ;
        }
    }


}

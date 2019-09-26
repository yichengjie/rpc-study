package com.yicj.study.rpc;

import com.yicj.study.handler.NettyClientHandler;
import com.yicj.study.vo.Request;
import com.yicj.study.vo.Response;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class ConnectManage {
    @Autowired
    private NettyClient nettyClient ;
    @Autowired
    private NettyClientHandler nettyClientHandler ;
    private Map<String, Channel> channelNodes = new ConcurrentHashMap<String, Channel>();
    private AtomicInteger roundRobin = new AtomicInteger(0);
    //异步执行线程池
    private ExecutorService asyncExecutorService = Executors.newFixedThreadPool(10);

    @PostConstruct
    private void init(){
        nettyClient.start();
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
    
    public Future<Response> sendRequestAsync(Request request) {
    	Channel channel = this.chooseChannel();
        if (channel != null && channel.isActive()) {
        	SynchronousQueue<Response> queue = nettyClientHandler.sendRequest(request, channel);
        	return asyncExecutorService.submit(() -> queue.take()) ;
        } else {
        	Response res = new Response();
            res.setCode(Response.ERROR);
            res.setErrorMsg("未正确连接到服务器.请检查相关配置信息!");
        	return asyncExecutorService.submit(() -> res) ;
        }
	}
    
    public void updateConnectServer(List<String> addressList) {
		if(addressList.isEmpty()) {
			log.error("没有可用的服务器节点, 全部服务节点已关闭!");
			this.closeAllConnect();
			return ;
		}
		for(String address: addressList) {
			Channel channel = channelNodes.get(address);
			if(channel!=null && channel.isOpen()) {
				 log.info("当前服务节点已存在,无需重新连接.{}",address);
			}else {
				 connectServerNode(address);
			}
		}
		//移除失效的节点
		Set<String> keySet = this.channelNodes.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			//如果新地址不包含旧地址，则直接移除
			if(!this.containsAddress(addressList, key)) {
				Channel channel = channelNodes.get(key);
				if(channel != null) {
				   channel.close();
	            }
				iterator.remove(); 
			}
		}
	}
    
    private Channel chooseChannel(){
    	Collection<Channel> values = channelNodes.values();
    	List<Channel> list = new ArrayList<Channel>(values) ;
        if (list.size()>0) {
            int size = list.size();
            int index = (roundRobin.getAndAdd(1) + size) % size;
            Channel channel = list.get(index);
            return channel ;
        }else{
            return null;
        }
    }

	
	
	private boolean containsAddress(List<String> addressList,String address) {
		for(String item : addressList) {
			if(item.equals(address)) {
				return true ;
			}
		}
		return false;
	}
	
	//连接服务端
	private void connectServerNode(String address) {
		String [] infos = address.split(":") ;
		Channel channel = nettyClient.doConnect(infos[0],Integer.parseInt(infos[1]));
		this.channelNodes.put(address, channel) ;
	}

	//关闭所有的channel连接
	private void closeAllConnect() {
		Collection<Channel> values = channelNodes.values();
		for(Channel channel : values) {
			channel.close() ;
		}
		channelNodes.clear();
	}

	

}

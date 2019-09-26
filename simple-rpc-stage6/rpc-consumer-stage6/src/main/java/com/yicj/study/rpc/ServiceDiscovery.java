package com.yicj.study.rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

@Component
public class ServiceDiscovery {
	private Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class) ;
	@Setter
	@Getter
    @Value("${registry.address}")
    private String registryAddress;
    private static final String registryPath = "/rpc";
    @Autowired
    private ConnectManage connectManage ;
    
    @PostConstruct
    private void init(){
        ZkClient zkClient = new ZkClient(registryAddress) ;
        List<String> nodeList = zkClient.subscribeChildChanges(registryPath, new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				logger.info("------------------------");
				logger.info("节点数据变化 : " + currentChilds);
				logger.info("------------------------");
				updateConnectedServer(currentChilds, zkClient);
			}
		}) ;
        updateConnectedServer(nodeList, zkClient);
    }
    
    
    private void updateConnectedServer(List<String> currentChilds, ZkClient zkClient) {
    	List<String> addressList = getNodeData(currentChilds,zkClient);
		connectManage.updateConnectServer(addressList);
    }
    
    private List<String> getNodeData(List<String> nodes,ZkClient zkClient){
        List<String> retList = new ArrayList<String>() ;
        for(String node:nodes){
            String address = zkClient.readData(registryPath+"/"+node);
            retList.add(address);
        }
        return retList ;
    }
    
    public static void main(String[] args) throws IOException {
    	String registryAddress = "127.0.0.1:2181" ;
    	ServiceDiscovery discovery = new ServiceDiscovery() ;
    	discovery.setRegistryAddress(registryAddress);
    	discovery.init(); 
    	System.in.read() ;
	}

}

package com.yicj.study.connection;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceDiscovery {
    @Value("${registry.address}")
    private String registryAddress;
    @Autowired
    private ConnectManage connectManage;

    // 服务地址列表
    private volatile List<String> addressList = new ArrayList<>();
    private static final String ZK_REGISTRY_PATH = "/rpc";
    private ZkClient client;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init(){
        client = connectServer();
        if (client != null) {
            watchNode(client);
        }
    }

    private ZkClient connectServer() {
        ZkClient client = new ZkClient(registryAddress,20000,20000);
        return client;
    }
    private void watchNode(final ZkClient client) {
        List<String> nodeList = client.subscribeChildChanges(ZK_REGISTRY_PATH, (s, nodes) -> {
            logger.info("监听到子节点数据变化{}",nodes);
            addressList.clear();
            getNodeData(nodes);
            updateConnectedServer();
        });
        getNodeData(nodeList);
        logger.info("已发现服务列表...{}", addressList);
        updateConnectedServer();
    }
    private void updateConnectedServer(){
        connectManage.updateConnectServer(addressList);
    }

    private void getNodeData(List<String> nodes){
        logger.info("/rpc子节点数据为:{}", nodes);
        for(String node:nodes){
            String address = client.readData(ZK_REGISTRY_PATH+"/"+node);
            addressList.add(address);
        }
    }
}
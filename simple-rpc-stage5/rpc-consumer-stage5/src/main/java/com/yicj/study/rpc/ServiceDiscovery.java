package com.yicj.study.rpc;

import com.yicj.study.util.ZookeeperUtils;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceDiscovery {
    @Value("${registry.address}")
    private String registryAddress;
    private static final String registryPath = "/registry";

    private void init(){
        ZooKeeper zk = ZookeeperUtils.connect(registryAddress) ;
    }

}

package com.yicj.study.rpc;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceRegistry implements InitializingBean {
    //注册中心地址
    @Value("${registry.address}")
    private String registryAddress ;
    //rpc服务提供地址
    @Value("${rpc.server.address}")
    private String rpcServiceAddress ;
    //服务器设定的目录
    private String registryPath = "/rpc";
    //地址目录
    private static String addressName = "address";
    private ZkClient zkClient ;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.zkClient = new ZkClient(registryAddress,5000);
    }

    //创建节点，也就是访问的，目录
    public void createNode()  {
    	 //----- /registry/com.yicj.demo.rpc.server.service.IHelloService/
        if(!zkClient.exists(registryPath)) {
        	zkClient.createPersistent(registryPath);
        }
        String addressPath = registryPath + "/" +addressName;
        //地址目录，这里ip就是本地的地址，用于tcp链接使用
        //这里创建的是临时目录，当zk服务断连过后，自动删除临时节点
        zkClient.createEphemeralSequential(addressPath, rpcServiceAddress) ;
    }

}

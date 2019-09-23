package com.yicj.study.rpc;

import com.yicj.study.util.ZookeeperUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ZkRegistry implements InitializingBean {
    //注册中心地址
    @Value("${registry.address}")
    private String registryAddress ;
    //rpc服务提供地址
    @Value("${rpc.server.address}")
    private String rpcServiceAddress ;
    //服务器设定的目录
    private String registryPath = "/registry";
    //地址目录
    private static String addressName = "address";
    private ZooKeeper zooKeeper ;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.zooKeeper = ZookeeperUtils.connect(registryAddress) ;
    }

    //创建节点，也就是访问的，目录
    public void createNode(String interfaceName)  {
        try {
            if (zooKeeper.exists(registryPath, false) == null) {
                //创建永久目录，接口服务，可以创建永久目录
                zooKeeper.create(registryPath, null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            //----- /registry/com.yicj.demo.rpc.server.service.IHelloService/
            String servicePath = registryPath + "/" +interfaceName;
            if (zooKeeper.exists(servicePath, false) == null) {//接口目录
                zooKeeper.create(servicePath, null,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            //----- /registry/com.yicj.demo.rpc.server.service.IHelloService/address
            //localhost:8000
            String addressPath = servicePath + "/" +addressName;
            //地址目录，这里ip就是本地的地址，用于tcp链接使用
            //这里创建的是临时目录，当zk服务断连过后，自动删除临时节点
            zooKeeper.create(addressPath, this.rpcServiceAddress.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        }catch (Exception e){
            throw new RuntimeException("注册服务到zk报错！",e) ;
        }
    }


}

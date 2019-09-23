package com.yicj.study.util;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

@Slf4j
public class ZookeeperUtils {
    public static ZooKeeper connect(String registryAddress) {
        ZooKeeper zooKeeper = null;
        CountDownLatch latch = new CountDownLatch(1);
        // 连接zk
        try {
            zooKeeper = new ZooKeeper(registryAddress, 60000, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();// 无连接阻塞
        } catch (Exception e) {
            log.error("获取连接报错!",e);
        }
        return zooKeeper;
    }
}

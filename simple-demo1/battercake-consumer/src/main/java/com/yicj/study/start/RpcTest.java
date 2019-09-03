package com.yicj.study.start;

import com.yicj.study.rpc.RpcConsumer;
import com.yicj.study.service.BatterCakeService;
//接下来建立一个测试类RpcTest如下（跑该测试类前，
//记得运行在battercake-provider端的RpcBootstrap类发布BatterCakeService服务）
public class RpcTest {
	public static void main(String[] args) {
		BatterCakeService batterCakeService = 
				RpcConsumer.getService(BatterCakeService.class, "127.0.0.1", 20006);
        String result = batterCakeService.sellBatterCake("双蛋");
        System.out.println(result);
	}
}

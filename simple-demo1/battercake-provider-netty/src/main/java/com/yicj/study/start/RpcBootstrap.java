package com.yicj.study.start;

import com.yicj.study.rpc.RpcProvider;
import com.yicj.study.service.BatterCakeService;
import com.yicj.study.service.impl.BatterCakeServiceImpl;

public class RpcBootstrap {

	public static void main(String[] args) throws Exception {
		BatterCakeService batterCakeService = new BatterCakeServiceImpl();
		// 发布卖煎饼的服务，注册在20006端口
		RpcProvider.export(20006, batterCakeService);
	}
}

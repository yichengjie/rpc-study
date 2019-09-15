package com.yicj.study.rpc;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.yicj.study.common.RpcRequestVo;
import com.yicj.study.common.RpcResponseVo;
import com.yicj.study.handler.ProxyHandler;
import com.yicj.study.service.BatterCakeService;
import com.yicj.study.util.IdUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcReqClientTest {
	private RpcReqClient rpcReqClient ;
	
	@Before
	public void before() throws InterruptedException {
		int port = 8080  ;
		String host = "127.0.0.1" ;
		rpcReqClient = new RpcReqClient().connect(host, port);
	}
	
	@Test
	public void testCallRemoteMethod() throws IOException, InterruptedException {
		String interfaceName = "com.yicj.study.service.BatterCakeService" ;
		String methodName = "sellBatterCake" ;
		Class<?>[] methodParameterTypes = new Class [] {String.class} ;
		Object[] methodParameters = new Object[] {"hello"} ;
		RpcRequestVo vo = new RpcRequestVo(IdUtil.getId(),interfaceName, methodName, methodParameterTypes, methodParameters) ;
		RpcResponseVo resp = rpcReqClient.sendRequest(vo);
		log.info("=======> " +resp.toString());
	}
	
	@Test
	public void testCallRemoteMethod2() throws IOException, InterruptedException {
		ProxyHandler proxyHandler = new ProxyHandler(rpcReqClient) ;
		BatterCakeService service = proxyHandler.getService(BatterCakeService.class);
		String retInfo = service.sellBatterCake("张三");
		log.info("=======> " +retInfo);
	}

}

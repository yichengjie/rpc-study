package com.yicj.study.config;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig {
	@Value("${registry.address}")
	private String registryAddress;
	
	@Bean
	public ZkClient zkClient() {
		ZkClient zkClient = new ZkClient(registryAddress);
		return zkClient ;
	}
}

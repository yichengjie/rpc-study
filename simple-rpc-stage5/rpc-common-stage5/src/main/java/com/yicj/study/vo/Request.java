package com.yicj.study.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String className;// 类名
	private String methodName;// 函数名称
	private Class<?>[] parameterTypes;// 参数类型
	private Object[] parameters;// 参数列表

	public static Request getHeartBeatRequest(){
		String methodName = "heartBeat" ;
		Request request = new Request(null,null,methodName,null,null) ;
		return  request ;
	}
}

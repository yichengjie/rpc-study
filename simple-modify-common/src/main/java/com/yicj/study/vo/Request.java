package com.yicj.study.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Request implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String className;// 类名
	private String methodName;// 函数名称
	private Class<?>[] parameterTypes;// 参数类型
	private Object[] parameters;// 参数列表
}

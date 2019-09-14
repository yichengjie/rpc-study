package com.yicj.study.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class RpcRequestVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String interfaceName ;
	private String methodName ;
	private Class<?>[] methodParameterTypes ;
	private Object[] methodParameters ;
}

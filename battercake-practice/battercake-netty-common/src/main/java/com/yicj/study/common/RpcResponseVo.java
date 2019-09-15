package com.yicj.study.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class RpcResponseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String reqId ;//请求id
	private String code ;
	private String msg ;
	private Object data ;
}

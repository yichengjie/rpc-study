package com.yicj.study.common;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RpcResponseVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code ;
	private String msg ;
	private Object data ;
}

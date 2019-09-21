package com.yicj.study.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Response implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Integer SUCCESS = 1 ;//成功
	public static Integer ERROR = -1 ; //出错
	private String requestId;
    private Integer code = SUCCESS;
    private String errorMsg;
    private Object data;
}

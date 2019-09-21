package com.yicj.study.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	private String requestId;
    private int code;
    private String errorMsg;
    private Object data;
}

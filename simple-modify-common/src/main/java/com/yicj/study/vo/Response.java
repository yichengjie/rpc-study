package com.yicj.study.vo;

import lombok.Data;

@Data
public class Response {
	private String requestId;
    private int code;
    private String errorMsg;
    private Object data;
}

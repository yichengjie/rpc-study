package com.yicj.study.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
    private String name;
    private String address;
}

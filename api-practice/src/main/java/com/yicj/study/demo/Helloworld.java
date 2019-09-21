package com.yicj.study.demo;

public class Helloworld {
	
	
	public static void main(String[] args) {
		getMethodName();
	}
	
	private static void getMethodName() {
//		String methodName = "helloAsync" ;
//		if(methodName.endsWith("Async")) {
//			methodName = methodName.substring(0, methodName.length() -5) ;
//		}
//		System.out.println(methodName);
		NullPointerException e = new NullPointerException();
		System.out.println(e.getMessage());

	}

}

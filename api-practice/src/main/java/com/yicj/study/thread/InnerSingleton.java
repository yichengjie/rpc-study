package com.yicj.study.thread;


//单例的方式
public class InnerSingleton {
	
	private static class Singletion {
		private Singletion() {
			System.out.println("ccccccccccccccc");
		}
		private static Singletion single = new Singletion() ;
		
	}
	
	public static Singletion getInstance() {
		return Singletion.single ;
	}
	
	public static void main(String[] args) {
		System.out.println("hello ");
		InnerSingleton.getInstance() ;
	}
}

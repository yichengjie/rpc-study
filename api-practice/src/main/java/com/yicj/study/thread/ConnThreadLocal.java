package com.yicj.study.thread;

import java.util.concurrent.TimeUnit;

public class ConnThreadLocal {
	
	private ThreadLocal<String> th = new ThreadLocal<String>() ;
	
	public void setTh(String value) {
		th.set(value);
	}
	public void getTh() {
		String str = th.get();
		System.out.println(Thread.currentThread().getName() +" : " + str);
	} 

	
	/**
	 * t1 : 张三
     * t2 : null
	 */
	public static void main(String[] args) throws InterruptedException {
		ConnThreadLocal ct = new ConnThreadLocal() ;
		new Thread(new Runnable() {
			@Override
			public void run() {
				ct.setTh("张三");
				ct.getTh();
			}
		},"t1").start(); ;
		new Thread(new Runnable() {
			@Override
			public void run() {
				//休息1秒钟
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ct.getTh();
			}
		},"t2").start(); ;
		
		
	}
	
}

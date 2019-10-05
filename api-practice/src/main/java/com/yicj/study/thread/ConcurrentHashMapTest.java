package com.yicj.study.thread;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class ConcurrentHashMapTest {
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(2);
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>() ;
		int len = 50 ;
		
		Thread t1  = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0 ; i < len ; i ++) {
					map.put("t1-key" + i, "t1-value" + i) ;
				}
				latch.countDown(); 
			}
		}) ;
		
		Thread t2  = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0 ; i < len ; i ++) {
					map.put("t2-key" + i, "t2-value" + i) ;
				}
				latch.countDown(); 
			}
		}) ;
		t1.start();
		t2.start(); 
		latch.await(); 
		Set<String> keySet = map.keySet();
		for(String key : keySet) {
			System.out.println(key);
		}
		System.out.println("size " + keySet.size());
		
		/**
		 *  t1-key0
			t1-key1
			t1-key2
			size 100
			多线程操作同一个map线程不安全时会导致问题
		 */
	}
	

}

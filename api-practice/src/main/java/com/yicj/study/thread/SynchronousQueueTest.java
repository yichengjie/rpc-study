package com.yicj.study.thread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class SynchronousQueueTest {

	public static void main(String[] args) throws InterruptedException {
		//test1无法运行
		//test1();
		//test2无法运行
		//test2();
		//test3正常运行
		test3();
	}

	private static void test1() throws InterruptedException {
		SynchronousQueue<String> queue = new SynchronousQueue<String>();
		String str = queue.take();//此时当前线程会一直阻塞，无法执行后面的代码
		System.out.println(str);
		queue.put("hello");
	}
	
	private static void test2() throws InterruptedException {
		SynchronousQueue<String> queue = new SynchronousQueue<String>();
		queue.put("hello");//此时当前线程会一直阻塞，无法执行后面的代码
		String str = queue.take();
		System.out.println(str);
		
	}
	
	private static void test3() {
		SynchronousQueue<String> queue = new SynchronousQueue<String>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String str = queue.take();
					System.out.println(str);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(1);
					queue.put("hello");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		;
	}
}

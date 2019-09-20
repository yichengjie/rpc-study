package com.yicj.study.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureCook {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		long startTime = System.currentTimeMillis() ;
		//第一步：网购厨具
		Callable<ChuJu> onlineShopping = new Callable<ChuJu>() {
			@Override
			public ChuJu call() throws Exception {
				System.out.println("第一步：下单");
				System.out.println("第一步：等待发货");
				Thread.sleep(5000);//模拟送货时间
				System.out.println("第一步：快递送到");
				return new ChuJu();
			}
		};
		FutureTask<ChuJu> task = new FutureTask<>(onlineShopping) ;
		new Thread(task).start(); 
		//第二步 去超市购买食材
		Thread.sleep(2000);//模拟购买食材时间
		ShiCai shiCai = new ShiCai() ;
		System.out.println("第二步：食材到位");
		//第三步 用厨具烹饪食材
		if(!task.isDone()) {//联系快递员，询问是否到货
			System.out.println("第三步：厨具还没到，心情好就等着(心情不好就掉cancel方法取消订单)");
		}
		ChuJu chuJu = task.get() ;
		System.out.println("第三步：厨具到位，开始展现厨艺");
		cook(chuJu, shiCai);
		System.out.println("总共用时:"+(System.currentTimeMillis() - startTime) +"ms");
	}
	
	
	
	static void cook(ChuJu chuJu,ShiCai shiCai) {
		
	}
	
	//厨具类
	static class ChuJu{
		
	}
	//食材类
	static class ShiCai{
		
	}
}

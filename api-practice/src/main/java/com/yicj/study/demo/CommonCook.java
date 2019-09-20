package com.yicj.study.demo;


public class CommonCook {
	
	public static void main(String[] args) throws InterruptedException {
		//CountDownLatch latch = new CountDownLatch(1) ;
		long startTime = System.currentTimeMillis() ;
		//第一步：网购厨具
		OnLineShopping thread = new OnLineShopping() ;
		thread.start(); 
		thread.join(); //保证厨具送到
		//第二步：去超市购买食材
		Thread.sleep(2000);//模拟购买食材时间
		ShiCai shiCai = new ShiCai() ;
		System.out.println("第二步:食材到位");
		//第三步：用厨具烹饪食材
		System.out.println("第三步：开始展现厨艺");
		cook(thread.getChuJu(), shiCai);
		System.out.println("总共用时"+(System.currentTimeMillis()-startTime) +"ms");
	}
	
	
	//网购厨具线程
	static class OnLineShopping extends Thread{
		private ChuJu chuJu ;
		@Override
		public void run() {
			System.out.println("第一步：下单");
			System.out.println("第一步：待发货");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("第一步：快递送到");
			chuJu = new ChuJu() ;
		}
		public ChuJu getChuJu() {
			return chuJu;
		}
		public void setChuJu(ChuJu chuJu) {
			this.chuJu = chuJu;
		}
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

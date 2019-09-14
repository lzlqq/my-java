package com.leo.java.threadlocal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest {
	/**
	 * 测试结果显示两次赋值，得到的结果还是第一次的值！为什么？
其实原因也很简单，我们的线程池会缓存使用过的线程。
当线程需要被重复利用的时候，并不会再重新执行init()初始化方法，
而是直接使用已经创建过的线程，所以这里的值不会二次产生变化，
那么该怎么做到真正的父子线程数据传递呢？
https://www.cnblogs.com/Nonnetta/p/10175662.html
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		ThreadLocal<String> local = new InheritableThreadLocal<>();
		ExecutorService executorService=Executors.newFixedThreadPool(1);
		CountDownLatch countDownLatch1=new CountDownLatch(1);
		CountDownLatch countDownLatch2=new CountDownLatch(1);
		
		local.set("我是主线程1");
		executorService.submit(()->{
			System.out.println(String.format("1Thread-%s", local.get()));
			countDownLatch1.countDown();
		});
		countDownLatch1.await();
		System.out.println(String.format("%s", local.get()));
		
		System.out.println("======================");
		System.out.println("======================");
		
		local.set("我是主线程2");
		executorService.submit(()->{
			System.out.println(String.format("2Thread-%s", local.get()));
			countDownLatch2.countDown();
		});
		countDownLatch2.await();
		System.out.println(String.format("%s", local.get()));
		
		executorService.shutdown();
	}
}

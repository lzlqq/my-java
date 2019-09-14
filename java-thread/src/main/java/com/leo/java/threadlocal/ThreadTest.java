package com.leo.java.threadlocal;

import java.util.stream.IntStream;
/**
 * ThreadLocal父子线程之间的数据传递问题 
 * 
 */
public class ThreadTest {
	public static void main(String[] args) {
		//testQuestion();
		//无法解决线程池问题
		testSolve();
	}

	private static void testSolve() {
		ThreadLocal<String> local = new InheritableThreadLocal<>();
		local.set("我是主线程");
		
		normalQuestion(local);

		parallelQuetion(local);
	}

	@SuppressWarnings("unused")
	private static void testQuestion() {
		ThreadLocal<String> local = new ThreadLocal<>();
		local.set("我是主线程");

		normalQuestion(local);

		parallelQuetion(local);
	}

	private static void parallelQuetion(ThreadLocal<String> local) {
		IntStream.range(1, 10).parallel().forEach(id -> {
			System.out.println(String.format("%s-%s", id, local.get()));
		});
	}

	private static void normalQuestion(ThreadLocal<String> local) {
		Thread thread = new Thread(() -> {
			System.out.println(local.get());
		});
		thread.start();
	}
}

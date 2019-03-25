package com.leo.java.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableTest {

    public static void main(String[] args) {
        System.out.println("main start");

        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        // Future<?> future = threadPool.submit(new MyRunnable()) ;
        //1.DelegatedExecutorService#submit
        //2.ThreadPoolExecutor#submit(AbstractExecutorService)
        //3.ThreadPoolExecutor#newTaskFor
        //4.FutureTask实现RunnableFuture
        //5.RunnableFuture接口继承Runnable, Future<V>
        //6.ThreadPoolExecutor#execute
        //7.ThreadPoolExecutor#addWorker
        //8.t.start
        //9.线程执行FutureTask#run方法
        Future<String> future = threadPool.submit(new MyCallable());
        try {
            // 这里会发生阻塞
            System.out.println(future.get());
        } catch (Exception e) {

        } finally {
            threadPool.shutdown();
        }
        System.out.println("main end");
    }


    static class MyCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            // 模拟耗时任务
            Thread.sleep(3000);
            System.out.println("MyCallable 线程：" + Thread.currentThread().getName());
            return "MyCallable";
        }
    }

    static class MyRunnable implements Runnable {

        @Override
        public void run() {
            // 模拟耗时任务
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("MyRunnable");
        }
    }
}
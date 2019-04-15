package com.leo.java.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableDemo implements Callable<Integer>{

    /**
     * 实现Callable并重写call方法作为线程执行体, 并设置返回值1
     */
    @Override
    public Integer call() throws Exception{
        System.out.println("Thread is running...");
        Thread.sleep(3000);
        return 1;
    }

    public static void main(String[] args) throws InterruptedException,ExecutionException{
        //创建Callable实现类的对象
        CallableDemo tc = new CallableDemo();
        //创建FutureTask类的对象
        FutureTask<Integer> task = new FutureTask<>(tc);
        //把FutureTask实现类对象作为target,通过Thread类对象启动线程
        new Thread(task).start();
        System.out.println("do something else...");
        //通过get方法获取返回值
        Integer integer = task.get();
        System.out.println("The thread running result is :" + integer);
    }
}
package com.leo.java.callable;

public class MyCallableDemo{

    /**
     * 1. 在Runnable实现类中，定义回调方法接口的引用，在构造方法中初始化。
     * 2. 在Runnable实现类中，在run方法中使用回调方法接口的引用， 来调用回调方法。
     * 3. 新建回调接口的实现类，重写回调接口。
     * 4. 在主程序中：新建的回调接口实现类对象；新建Runnable实现类对象，传入回调接口实现类对象；新建线程类对象是，传入Runnable实现类对象，启动线程。
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException{
        String fileName = "D:\\test\\create.txt";
        MyCallable mc = new MyCallableImpl();
        new Thread(new MyRunnableTask(fileName, mc)).start();
        System.out.println("初始：" + mc.getResult());
        Thread.sleep(10000L);
        System.out.println("10s后：" + mc.getResult());
    }

}

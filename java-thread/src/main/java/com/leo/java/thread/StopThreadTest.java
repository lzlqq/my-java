package com.leo.java.thread;

import java.util.concurrent.TimeUnit;

/**
 * 首先,导致一个线程长时间运行的原因无非有这么几个:
 *      代码逻辑混乱\业务复杂,写了一个很长的run()方法
 *      长时间睡眠
 *      循环
 * 对于这第一种嘛,只能从代码结构上优化了.
 * 而这第二种,就要使用Thread.interrupt()方法了.这个方法唤醒睡眠中的线程:
 * 在bio的服务端里面,会阻塞当前线程.监听的端口有消息,才会继续执行,而如果没有人连接,就需要通过这种方式来打断正在监听的线程.
 *
 */
public class StopThreadTest {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.DAYS.sleep(1L);
                    System.out.println("睡眠结束");
                } catch (Exception e) {
                    System.out.println("异常:" + e);
                } finally {
                    System.out.println("finally块被执行");
                }
            }
        });

        thread.start();

        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
        System.out.println("主线程结束");
    }
}

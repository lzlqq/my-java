package com.leo.java.concurrence;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by LSH7120 on 2019/3/6.
 */
public class CASTest{

    //public static volatile int race=0;
    public static AtomicInteger race = new AtomicInteger(0);

    public static final int THREAD_COUNT = 20;

    public static void increase(){
        //race++; //非原子操作
        race.getAndIncrement();//不用加synchronize，这是个原子操作
    }

    public static void main(String[] args){
        Thread[] threads = new Thread[THREAD_COUNT];
        for (int i = 0; i < threads.length; i++){
            threads[i] = new Thread(new Runnable(){

                @Override
                public void run(){
                    for (int j = 0; j < 1000; j++){
                        increase();
                        // System.out.println(race);
                    }
                }
            });
            threads[i].start();
        }
        while (Thread.activeCount() > 2){
            System.out.println(Thread.activeCount());
            Thread.yield();
        }
        System.out.println(race);
    }
}

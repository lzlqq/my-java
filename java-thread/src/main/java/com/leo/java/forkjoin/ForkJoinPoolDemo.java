package com.leo.java.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * 批量发送消息,这种场景不需要有返回值
 * ForkJoinTask两个子类RecursiveAction和RecursiveTask
 */
public class ForkJoinPoolDemo{

    class SendMsgTask extends RecursiveAction{

        private static final long serialVersionUID = -3596471864655808248L;

        private final int THRESHOLD = 10;

        private int start;

        private int end;

        private List<String> list;

        public SendMsgTask(int start, int end, List<String> list){
            super();
            this.start = start;
            this.end = end;
            this.list = list;
        }

        @Override
        protected void compute(){
            if ((end - start) < THRESHOLD){
                for (int i = start; i < end; i++){
                    System.out.println(Thread.currentThread().getName() + ":" + list.get(i));
                }
            }else{
                int middle = (end + start) / 2;
                invokeAll(new SendMsgTask(start, middle, list), new SendMsgTask(middle, end, list));
            }
        }

    }

    public static void main(String[] args) throws InterruptedException{
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 123; i++){
            list.add(String.valueOf(i));
        }
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new ForkJoinPoolDemo().new SendMsgTask(0, list.size(), list));
        pool.awaitTermination(10, TimeUnit.SECONDS);
        pool.shutdown();
    }
}

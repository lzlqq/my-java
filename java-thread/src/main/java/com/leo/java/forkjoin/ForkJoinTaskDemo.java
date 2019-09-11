package com.leo.java.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
/**
 * 求和，这种场景需要有返回值
 *  
 */
public class ForkJoinTaskDemo{

    class SumTask extends RecursiveTask<Integer>{

        private static final long serialVersionUID = 7017207564437713774L;

        private static final int THRESHOLD = 20;

        private int arr[];

        private int start;

        private int end;

        public SumTask(int start, int end, int arr[]){
            super();
            this.start = start;
            this.end = end;
            this.arr = arr;
        }
        /**
         * 小计
         */
        private Integer subtotal(){
            Integer sum = 0;
            for (int i = start; i < end; i++){
                sum += arr[i];
            }
            System.out.println(Thread.currentThread().getName() + ": ∑(" + start + "~" + end + ")=" + sum);
            return sum;
        }

        @Override
        protected Integer compute(){
            if ((end - start) <= THRESHOLD){
                return subtotal();
            }else{
                int middle = (start + end) / 2;
                SumTask left = new SumTask(start, middle, arr);
                SumTask right = new SumTask(middle, end, arr);
                left.fork();
                right.fork();

                return left.join() + right.join();
            }
        }

    }

    public static void main(String[] args){
        int arr[] = new int[100];
        for (int i = 0; i < 100; i++){
            arr[i] = i + 1;
        }
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Integer> result = pool.submit(new ForkJoinTaskDemo().new SumTask(0, arr.length, arr));
        System.out.println("最终计算结果：" + result.invoke());
        pool.shutdown();
    }
}

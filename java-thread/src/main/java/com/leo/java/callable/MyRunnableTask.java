package com.leo.java.callable;

import java.io.FileInputStream;

/**
 * 使用接口回调, 实现线程执行结果的返回
 * 
 * @author LSH7120
 *
 */
public class MyRunnableTask implements Runnable{

    private String fileName;

    private String result;

    /**
     * 定义回调方法接口的引用
     */
    private MyCallable mc;

    public MyRunnableTask(String fileName, MyCallable mc){
        this.fileName = fileName;
        this.mc = mc;
    }

    @Override
    public void run(){
        try (FileInputStream fis = new FileInputStream(fileName)){
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            while ((hasRead = fis.read(buffer)) > 0){
                Thread.sleep(1000L);
                result = new String(buffer, 0, hasRead);
            }
            //通过回调接口引用, 调用了receiveResult方法, 可以在主线程中返回结果.
            //此处利用了多态
            mc.call(fileName, result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

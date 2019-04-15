package com.leo.java.callable;

public interface MyCallable{

    /**
     * 只定义了回调方法, 传入一个待读取的文件名参数, 和返回结果
     * 
     * @param fileName
     * @param result
     */
    public void call(String fileName,String result);
    
    public String getResult();

}

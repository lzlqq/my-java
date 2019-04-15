package com.leo.java.callable;

public class MyCallableImpl implements MyCallable{

    private String result;

    /**
     * 实现回调方法
     */
    @Override
    public void call(String fileName,String result){
        System.out.println("call方法执行中，文件" + fileName + "的内容是: " + result);
        this.result = result;
    }

    public String getResult(){
        return result;
    }

    public void setResult(String result){
        this.result = result;
    }

}

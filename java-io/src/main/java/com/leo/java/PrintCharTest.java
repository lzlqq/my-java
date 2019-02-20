package com.leo.java;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;

/**
 * 1.java 使用Unicode存储字符串，在写入字符流时我们都可以指定写入的字符串的编码。
 * 前面介绍了不用抛异常的处理字节型数据的流ByteArrayOutputStream，
 * 与之对应的操作字符类的类就是CharArrayReader,CharArrayWriter类，
 * 这里也会用到缓冲区，不过是字符缓冲区，一般讲字符串放入到操作字符的io流一般方法是
 * CharArrayReader reader=mew CharArrayReader(str.toCharArray());
 * 一旦会去到CharArrayReader实例就可以使用CharArrayReader访问字符串的各个元素以执行进一步读取操作。不做例子
 * 2.我们用FileReader ，PrintWriter来做示范
 */
public class PrintCharTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO自动生成的方法存根
        char[] buffer = new char[512];   //一次取出的字节数大小,缓冲区大小
        int numberRead = 0;
        FileReader reader = null;        //读取字符文件的流
        PrintWriter writer = null;    //写字符到控制台的流

        try {
            reader = new FileReader("D:/David/Java/java 高级进阶/files/copy1.txt");
            writer = new PrintWriter(System.out);  //PrintWriter可以输出字符到文件，也可以输出到控制台
            while ((numberRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, numberRead);
            }
        } catch (IOException e) {
            // TODO自动生成的 catch 块
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // TODO自动生成的 catch 块
                e.printStackTrace();
            }
            writer.close();       //这个不用抛异常
        }

    }

}
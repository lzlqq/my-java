package com.leo.java;

import java.io.*;

/**
 * 文件
 */
public class FileTest {
    public static void main(String[] args) {
        createFile();
    }

    /**
     * 文件处理示例
     */
    public static void createFile() {
        File file = new File("D:/test/create/hhh/bbb.tt");
        try {
            if (!file.getParentFile().exists() && !file.isDirectory()) {// 如果文件夹不存在则创建
                file.getParentFile().mkdirs();
                file.createNewFile();// 当且仅当不存在具有此抽象路径名指定名称的文件时，才会创建一个新的空文件
            } else {
                file.createNewFile();// 当且仅当不存在具有此抽象路径名指定名称的文件时，才会创建一个新的空文件。
            }
            System.out.println("该分区大小" + file.getTotalSpace() / (1024 * 1024 * 1024) + "G"); // 返回由此抽象路径名表示的文件或目录的大小。
            // file.mkdirs();  // 创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
            // file.delete(); // 删除此抽象路径名表示的文件或目录
            System.out.println("文件名  " + file.getName());  //  返回由此抽象路径名表示的文件或目录的名称。
            System.out.println("文件父目录字符串 " + file.getParent());// 返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null。
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}  
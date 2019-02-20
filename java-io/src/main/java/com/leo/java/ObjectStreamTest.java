package com.leo.java;

import java.io.*;

/**
 * 读写对象：ObjectInputStream 和ObjectOutputStream ，
 * 该流允许读取或写入用户自定义的类，
 * 但是要实现这种功能，被读取和写入的类必须实现Serializable接口，
 * 其实该接口并没有什么方法，可能相当于一个标记而已，但是确实不合缺少的
 */
public class ObjectStreamTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO自动生成的方法存根
        ObjectOutputStream objectwriter = null;
        ObjectInputStream objectreader = null;

        try {
            objectwriter = new ObjectOutputStream(new FileOutputStream("D:/test/student.txt"));
            objectwriter.writeObject(new Student("gg", 22));
            objectwriter.writeObject(new Student("tt", 18));
            objectwriter.writeObject(new Student("rr", 17));
            objectreader = new ObjectInputStream(new FileInputStream("D:/test/student.txt"));
            for (int i = 0; i < 3; i++) {
                System.out.println(objectreader.readObject());
            }
        } catch (IOException | ClassNotFoundException e) {
            // TODO自动生成的 catch 块
            e.printStackTrace();
        } finally {
            try {
                objectreader.close();
                objectwriter.close();
            } catch (IOException e) {
                // TODO自动生成的 catch 块
                e.printStackTrace();
            }

        }

    }

    static class Student implements Serializable {
        private String name;
        private int age;

        public Student(String name, int age) {
            super();
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student [name=" + name + ", age=" + age + "]";
        }
    }


}
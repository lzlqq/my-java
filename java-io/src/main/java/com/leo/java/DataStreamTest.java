package com.leo.java;

import java.io.*;

/**
 * 有时没有必要存储整个对象的信息，而只是要存储一个对象的成员数据，
 * 成员数据的类型假设都是Java的基本数据类型，这样的需求不必使用到与Object输入、输出相关的流对象，
 * 可以使用DataInputStream、DataOutputStream来写入或读出数据。
 * 下面是一个例子：（
 * DataInputStream的好处在于在从文件读出数据时，
 * 不用费心地自行判断读入字符串时或读入int类型时何时将停止，
 * 使用对应的readUTF()和readInt()方法就可以正确地读入完整的类型数据。
 * ）
 */
public class DataStreamTest {
    public static void main(String[] args) {
        Member[] members = {new Member("Justin", 90),
                new Member("momor", 95),
                new Member("Bush", 88)};
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("D:/test/member.txt"));

            for (Member member : members) {
                //写入UTF字符串
                dataOutputStream.writeUTF(member.getName());
                //写入int数据
                dataOutputStream.writeInt(member.getAge());
            }

            //所有数据至目的地
            dataOutputStream.flush();
            //关闭流
            dataOutputStream.close();

            DataInputStream dataInputStream = new DataInputStream(new FileInputStream("D:/test/member.txt"));

            //读出数据并还原为对象
            for (int i = 0; i < members.length; i++) {
                //读出UTF字符串
                String name = dataInputStream.readUTF();
                //读出int数据
                int score = dataInputStream.readInt();
                members[i] = new Member(name, score);
            }

            //关闭流
            dataInputStream.close();

            //显示还原后的数据
            for (Member member : members) {
                System.out.printf("%s\t%d%n", member.getName(), member.getAge());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class Member {
        private String name;
        private int age;

        public Member() {
        }

        public Member(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
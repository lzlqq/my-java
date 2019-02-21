package com.leo.java.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *JAVA处理大文件，一般用BufferedReader,BufferedInputStream这类带缓冲的IO类，不过如果文件超大的话，更快的方式是采用MappedByteBuffer。
 *
 * MappedByteBuffer是NIO引入的文件内存映射方案，读写性能极高。
 * NIO最主要的就是实现了对异步操作的支持。
 * 其中一种通过把一个套接字通道(SocketChannel)注册到一个选择器(Selector)中,
 * 不时调用后者的选择(select)方法就能返回满足的选择键(SelectionKey),键中包含了SOCKET事件信息。这就是select模型。
 *
 * SocketChannel的读写是通过一个类叫ByteBuffer来操作的.
 * 这个类本身的设计是不错的,比直接操作byte[]方便多了.
 * ByteBuffer有两种模式:
 * 直接/间接.间接模式最典型(也只有这么一种)的就是HeapByteBuffer,即操作堆内存 (byte[]).
 * 但是内存毕竟有限,如果我要发送一个1G的文件怎么办?不可能真的去分配1G的内存.这时就必须使用"直接"模式,即 MappedByteBuffer,文件映射.
 *
 * 先中断一下,谈谈操作系统的内存管理.
 * 一般操作系统的内存分两部分:
 * 物理内存;虚拟内存.
 * 虚拟内存一般使用的是页面映像文件,即硬盘中的某个(某些)特殊的文件.
 * 操作系统负责页面文件内容的读写,这个过程叫"页面中断/切换".
 * MappedByteBuffer也是类似的,你可以把整个文件(不管文件有多大)看成是一个ByteBuffer.
 * MappedByteBuffer 只是一种特殊的ByteBuffer，即是ByteBuffer的子类。
 * MappedByteBuffer 将文件直接映射到内存（这里的内存指的是虚拟内存，并不是物理内存）。
 * 通常，可以映射整个文件，如果文件比较大的话可以分段进行映射，只要指定文件的那个部分就可以。
 *
 * 概念
 * FileChannel提供了map方法来把文件影射为内存映像文件：
 *      MappedByteBuffer map(int mode,long position,long size);
 * 可以把文件的从position开始的size大小的区域映射为内存映像文件，mode指出了 可访问该内存映像文件的方式：
 *      READ_ONLY,（只读）： 试图修改得到的缓冲区将导致抛出 ReadOnlyBufferException.(MapMode.READ_ONLY)
 *      READ_WRITE（读/写）： 对得到的缓冲区的更改最终将传播到文件；该更改对映射到同一文件的其他程序不一定是可见的。 (MapMode.READ_WRITE)
 *      PRIVATE（专用）： 对得到的缓冲区的更改不会传播到文件，并且该更改对映射到同一文件的其他程序也不是可见的；相反，会创建缓冲区已修改部分的专用副本。 (MapMode.PRIVATE)
 *
 * MappedByteBuffer是ByteBuffer的子类，其扩充了三个方法：
 *      force()：缓冲区是READ_WRITE模式下，此方法对缓冲区内容的修改强行写入文件；
 *      load()：将缓冲区的内容载入内存，并返回该缓冲区的引用；
 *      isLoaded()：如果缓冲区的内容在物理内存中，则返回真，否则返回假；
 * 案例对比
 * 这里通过采用ByteBuffer和MappedByteBuffer分别读取大小约为5M的文件"src/1.ppt"来比较两者之间的区别，method3()是采用MappedByteBuffer读取的，method4()对应的是ByteBuffer。
 */
public class MappedByteBufferTest {
    public static void method4() {
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        try {
            aFile = new RandomAccessFile("src/1.ppt", "rw");
            fc = aFile.getChannel();
            long timeBegin = System.currentTimeMillis();
            ByteBuffer buff = ByteBuffer.allocate((int) aFile.length());
            buff.clear();
            fc.read(buff);
            //System.out.println((char)buff.get((int)(aFile.length()/2-1)));
            //System.out.println((char)buff.get((int)(aFile.length()/2)));
            //System.out.println((char)buff.get((int)(aFile.length()/2)+1));
            long timeEnd = System.currentTimeMillis();
            System.out.println("Read time: " + (timeEnd - timeBegin) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注：MappedByteBuffer有资源释放的问题：
     * 被MappedByteBuffer打开的文件只有在垃圾收集时才会被关闭，而这个点是不确定的。
     * 在Javadoc中这里描述：
     * A mapped byte buffer and the file mapping that it represents remian valid until the buffer itself is garbage-collected。
     */
    public static void method3() {
        RandomAccessFile aFile = null;
        FileChannel fc = null;
        try {
            aFile = new RandomAccessFile("src/1.ppt", "rw");
            fc = aFile.getChannel();
            long timeBegin = System.currentTimeMillis();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, aFile.length());
            // System.out.println((char)mbb.get((int)(aFile.length()/2-1)));
            // System.out.println((char)mbb.get((int)(aFile.length()/2)));
            //System.out.println((char)mbb.get((int)(aFile.length()/2)+1));
            long timeEnd = System.currentTimeMillis();
            System.out.println("Read time: " + (timeEnd - timeBegin) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
                if (fc != null) {
                    fc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

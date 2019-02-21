package com.leo.java.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中Buffer的使用
 * 从例中可以总结出使用Buffer一般遵循下面几个步骤：
 *      分配空间（ByteBuffer buf = ByteBuffer.allocate(1024); 还有一种allocateDirector后面再陈述）
 *      写入数据到Buffer(int bytesRead = fileChannel.read(buf);)
 *      调用filp()方法（ buf.flip();）
 *      从Buffer中读取数据（System.out.print((char)buf.get());）
 *      调用clear()方法或者compact()方法
 * Buffer顾名思义：缓冲区，实际上是一个容器，一个连续数组。Channel提供从文件、网络读取数据的渠道，但是读写的数据都必须经过Buffer。
 * 向Buffer中写数据：
 *      从Channel写到Buffer (fileChannel.read(buf))
 *      通过Buffer的put()方法 （buf.put(…)）
 * 从Buffer中读取数据：
 *      从Buffer读取到Channel (channel.write(buf))
 *      使用get()方法从Buffer中读取数据 （buf.get()）
 * 可以把Buffer简单地理解为一组基本数据类型的元素列表，它通过几个变量来保存这个数据的当前位置状态：capacity, position, limit, mark：
 *      索引                    说明
 *      capacity                缓冲区数组的总长度
 *      position                下一个要操作的数据元素的位置
 *      limit                   缓冲区数组中不可操作的下一个元素的位置：limit<=capacity
 *      mark                    用于记录当前position的前一个位置或者默认是-1
 * 举例：我们通过ByteBuffer.allocate(11)方法创建了一个11个byte的数组的缓冲区，position的位置为0，capacity和limit默认都是数组长度。
 * 这时我们需要将缓冲区中的5个字节数据写入Channel的通信信道，所以我们调用ByteBuffer.flip()方法，变化如下(position设回0，并将limit设成之前的position的值)
 * 这时底层操作系统就可以从缓冲区中正确读取这个5个字节数据并发送出去了。在下一次写数据之前我们再调用clear()方法，缓冲区的索引位置又回到了初始位置。
 * 调用clear()方法：position将被设回0，limit设置成capacity，
 * 换句话说，Buffer被清空了，其实Buffer中的数据并未被清除，只是这些标记告诉我们可以从哪里开始往Buffer里写数据。
 * 如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有。
 * 如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先写些数据，那么使用compact()方法。
 * compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
 * limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
 * 通过调用Buffer.mark()方法，可以标记Buffer中的一个特定的position，
 * 之后可以通过调用Buffer.reset()方法恢复到这个position。
 * Buffer.rewind()方法将position设回0，所以你可以重读Buffer中的所有数据。
 * limit保持不变，仍然表示能从Buffer中读取多少个元素。
 * */
public class FileChannelTest {
    public static void method1() {
        RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("src/nio.txt", "rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int bytesRead = fileChannel.read(buf);
            System.out.println(bytesRead);
            while (bytesRead != -1) {
                buf.flip();
                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }
                buf.compact();
                bytesRead = fileChannel.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (aFile != null) {
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

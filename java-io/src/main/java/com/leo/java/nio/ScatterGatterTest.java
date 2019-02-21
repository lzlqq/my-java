package com.leo.java.nio;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
/**
 * 分散（scatter）从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据“分散（scatter）”到多个Buffer中。
 * 聚集（gather）写入Channel是指在写操作时将多个buffer的数据写入同一个Channel，因此，Channel 将多个Buffer中的数据“聚集（gather）”后发送到Channel。
 * scatter / gather经常用于需要将传输的数据分开处理的场合，
 * 例如传输一个由消息头和消息体组成的消息，你可能会将消息体和消息头分散到不同的buffer中，这样你可以方便的处理消息头和消息体。
 */
public class ScatterGatterTest {


        public static void main(String args[]){
            gather();
        }
        public static void gather()
        {
            ByteBuffer header = ByteBuffer.allocate(10);
            ByteBuffer body = ByteBuffer.allocate(10);
            byte [] b1 = {'0', '1'};
            byte [] b2 = {'2', '3'};
            header.put(b1);
            body.put(b2);
            ByteBuffer [] buffs = {header, body};
            try
            {
                FileOutputStream os = new FileOutputStream("D:/test/scattingAndGather.txt");
                FileChannel channel = os.getChannel();
                channel.write(buffs);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


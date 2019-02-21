package com.leo.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 *到目前为止，所举的案例中都没有涉及Selector。
 * Selector类可以用于避免使用阻塞式客户端中很浪费资源的“忙等”方法。
 * 例如，考虑一个IM服务器。像QQ或者旺旺这样的，可能有几万甚至几千万个客户端同时连接到了服务器，但在任何时刻都只是非常少量的消息。
 * 需要读取和分发。
 * 这就需要一种方法阻塞等待，直到至少有一个信道可以进行I/O操作，并指出是哪个信道。
 * NIO的选择器就实现了这样的功能。
 * 一个Selector实例可以同时检查一组信道的I/O状态。
 * 用专业术语来说，选择器就是一个多路开关选择器，因为一个选择器能够管理多个信道上的I/O操作。
 *
 * 然而如果用传统的方式来处理这么多客户端，使用的方法是循环地一个一个地去检查所有的客户端是否有I/O操作，
 * 如果当前客户端有I/O操作，则可能把当前客户端扔给一个线程池去处理，
 * 如果没有I/O操作则进行下一个轮询，当所有的客户端都轮询过了又接着从头开始轮询；
 * 这种方法是非常笨而且也非常浪费资源，因为大部分客户端是没有I/O操作，我们也要去检查；
 *
 * 而Selector就不一样了，它在内部可以同时管理多个I/O，
 * 当一个信道有I/O操作的时候，他会通知Selector，
 * Selector就是记住这个信道有I/O操作，并且知道是何种I/O操作，是读呢？是写呢？还是接受新的连接；
 *
 * 所以如果使用Selector，它返回的结果只有两种结果，一种是0，即在你调用的时刻没有任何客户端需要I/O操作，
 * 另一种结果是一组需要I/O操作的客户端，这时你就根本不需要再检查了，因为它返回给你的肯定是你想要的。
 * 这样一种通知的方式比那种主动轮询的方式要高效得多！
 *
 * 要使用选择器（Selector），
 * 需要创建一个Selector实例（使用静态工厂方法open()）并将其注册（register）到想要监控的信道上（注意，这要通过channel的方法实现，而不是使用selector的方法）。
 * 最后，调用选择器的select()方法。
 * 该方法会阻塞等待，直到有一个或更多的信道准备好了I/O操作或等待超时。
 * select()方法将返回可进行I/O操作的信道数量。
 * 现在，在一个单独的线程中，通过调用select()方法就能检查多个信道是否准备好进行I/O操作。
 * 如果经过一段时间后仍然没有信道准备好，select()方法就会返回0，并允许程序继续执行其他任务。
 *
 * 下面将上面的TCP服务端代码改写成NIO的方式：
 */
public class ServerSocketChannelTest {


    private static final int BUF_SIZE = 1024;
    private static final int PORT = 8080;
    private static final int TIMEOUT = 3000;

    public static void main(String[] args) {
        selector();
    }

    public static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssChannel.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));//监听新进来的连接
    }

    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        long bytesRead = sc.read(buf);
        while (bytesRead > 0) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            System.out.println();
            buf.clear();
            bytesRead = sc.read(buf);
        }
        if (bytesRead == -1) {
            sc.close();
        }
    }

    public static void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel) key.channel();
        while (buf.hasRemaining()) {
            sc.write(buf);
        }
        buf.compact();
    }

    /**
     * 1 ServerSocketChannel
     *
     * 监听新进来的连接
     *  while(true){
     *      SocketChannel socketChannel = serverSocketChannel.accept();
     * }
     *
     * ServerSocketChannel可以设置成非阻塞模式。
     * 在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
     * 因此，需要检查返回的SocketChannel是否是null.如：
     * ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
     * serverSocketChannel.socket().bind(new InetSocketAddress(9999));
     * serverSocketChannel.configureBlocking(false);
     *  while (true)
     * {
     *      SocketChannel socketChannel = serverSocketChannel.accept();
     *      if (socketChannel != null)
     *      {
     *          // do something with socketChannel...
     *      }
     * }
     *
     * Selector
     *
     * Selector的创建：Selector selector = Selector.open();
     * 为了将Channel和Selector配合使用，必须将Channel注册到Selector上，通过SelectableChannel.register()方法来实现
     *
     * 与Selector一起使用时，Channel必须处于非阻塞模式下。
     * 这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式。而套接字通道都可以。
     *
     * 注意register()方法的第二个参数。这是一个“interest集合”，意思是在通过Selector监听Channel时对什么事件感兴趣。可以监听四种不同类型的事件：
     *      1. Connect
     *      2. Accept
     *      3. Read
     *      4. Write
     * 通道触发了一个事件意思是该事件已经就绪。
     * 所以，某个channel成功连接到另一个服务器称为“连接就绪”。
     * 一个server socket channel准备好接收新进入的连接称为“接收就绪”。
     * 一个有数据可读的通道可以说是“读就绪”。
     * 等待写数据的通道可以说是“写就绪”。
     * 这四种事件用SelectionKey的四个常量来表示：
     *      1. SelectionKey.OP_CONNECT
     *      2. SelectionKey.OP_ACCEPT
     *      3. SelectionKey.OP_READ
     *      4. SelectionKey.OP_WRITE
     *
     * SelectionKey
     *
     * 当向Selector注册Channel时，register()方法会返回一个SelectionKey对象。这个对象包含了一些你感兴趣的属性：
     *      interest集合
     *      ready集合
     *      Channel
     *      Selector
     *      附加的对象（可选）
     * interest集合：
     *      就像向Selector注册通道一节中所描述的，interest集合是你所选择的感兴趣的事件集合。可以通过SelectionKey读写interest集合。
     * ready 集合:
     *      是通道已经准备就绪的操作的集合。在一次选择(Selection)之后，你会首先访问这个ready set。可以这样访问ready集合：
     *      int readySet = selectionKey.readyOps();
     *
     * 可以用像检测interest集合那样的方法，来检测channel中什么事件或操作已经就绪。但是，也可以使用以下四个方法，它们都会返回一个布尔类型：
     *      selectionKey.isAcceptable();
     *      selectionKey.isConnectable();
     *      selectionKey.isReadable();
     *      selectionKey.isWritable();
     *
     * 从SelectionKey访问Channel和Selector很简单。如下：
     *      Channel  channel  = selectionKey.channel();
     *      Selector selector = selectionKey.selector();
     *
     * 可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道。
     * 例如，可以附加 与通道一起使用的Buffer，或是包含聚集数据的某个对象。使用方法如下：
     *      selectionKey.attach(theObject);
     *      Object attachedObj = selectionKey.attachment();
     * 还可以在用register()方法向Selector注册Channel的时候附加对象。如：
     *      SelectionKey key = channel.register(selector, SelectionKey.OP_READ, theObject);
     *
     * 通过Selector选择通道
     * 一旦向Selector注册了一或多个通道，就可以调用几个重载的select()方法。
     * 这些方法返回你所感兴趣的事件（如连接、接受、读或写）已经准备就绪的那些通道。
     * 换句话说，如果你对“读就绪”的通道感兴趣，select()方法会返回读事件已经就绪的那些通道。
     *
     * 下面是select()方法：
     *      int select()
     *      int select(long timeout)
     *      int selectNow()
     *  select()阻塞到至少有一个通道在你注册的事件上就绪了。
     *  select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)。
     *  selectNow()不会阻塞，不管什么通道就绪都立刻返回（此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。）。
     *
     * select()方法返回的int值表示有多少通道已经就绪。
     * 亦即，自上次调用select()方法后有多少通道变成就绪状态。
     * 如果调用select()方法，因为有一个通道变成就绪状态，返回了1，
     * 若再次调用select()方法，如果另一个通道就绪了，它会再次返回1。
     * 如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通道，但在每次select()方法调用之间，只有一个通道就绪了。
     *
     *  一旦调用了select()方法，并且返回值表明有一个或更多个通道就绪了，
     *  然后可以通过调用selector的selectedKeys()方法，访问“已选择键集（selected key set）”中的就绪通道。如下所示：
     *      Set selectedKeys = selector.selectedKeys();
     * 当向Selector注册Channel时，Channel.register()方法会返回一个SelectionKey 对象。这个对象代表了注册到该Selector的通道。
     *
     * 注意：
     *  每次迭代末尾的keyIterator.remove()调用。
     *  Selector不会自己从已选择键集中移除SelectionKey实例。必须在处理完通道时自己移除。
     *  下次该通道变成就绪时，Selector会再次将其放入已选择键集中。
     *
     * SelectionKey.channel()方法返回的通道需要转型成你要处理的类型，如ServerSocketChannel或SocketChannel等。
     * 一个完整的使用Selector和ServerSocketChannel的案例可以参考案例的selector()方法。
     */
    public static void selector() {
        Selector selector = null;
        ServerSocketChannel ssc = null;
        try {
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select(TIMEOUT) == 0) {
                    System.out.println("==");
                    continue;
                }
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    if (key.isWritable() && key.isValid()) {
                        handleWrite(key);
                    }
                    if (key.isConnectable()) {
                        System.out.println("isConnectable = true");
                    }
                    iter.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (selector != null) {
                    selector.close();
                }
                if (ssc != null) {
                    ssc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



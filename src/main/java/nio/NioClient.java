package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NioClient implements Runnable {

    private final ByteBuffer buffer = ByteBuffer.allocate(256);

    public static void main(String[] args) {
        new Thread(new NioClient()).start();
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        try {
            SocketChannel channel = SocketChannel.open(
                    new InetSocketAddress("localhost", 8189));
            while (channel.isOpen()) {
                channel.read(buffer);
                System.out.println(buffer);
                buffer.flip();
                String message = in.nextLine();
                ByteBuffer buf = ByteBuffer.wrap(message.getBytes());
                //buf.flip();
                channel.write(buf);

            }
//            Selector selector = Selector.open();
//            channel.connect(new InetSocketAddress("localhost", 8189));
//            channel.configureBlocking(false);
//            // channel.register(selector, SelectionKey.OP_CONNECT);
//            channel.register(selector, SelectionKey.OP_WRITE);
//            // channel.register(selector, SelectionKey.OP_READ);
//            while (channel.isOpen()) {
//                selector.select();
//                // ctrl + alt + v
//                Iterator<SelectionKey> iterator =
//                        selector.selectedKeys().iterator();
//                while (iterator.hasNext()) {
//                    SelectionKey key = iterator.next();
//                    if (key.isWritable()) {
//                        handleWrite(key, selector);
//                    }
//                    iterator.remove();
//                }
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleWrite(SelectionKey key, Selector selector) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.write(ByteBuffer.wrap("Hello".getBytes()));
    }
}

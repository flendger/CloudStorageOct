package netty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ByteBufInputHandler extends ChannelInboundHandlerAdapter {

    public static final ConcurrentLinkedDeque<SocketChannel> channels = new ConcurrentLinkedDeque<>();

    static void broadCastMessage(ByteBuf message) {
        for (var c : channels) {
            System.out.println("write msg: " + c + " " + message.toString(StandardCharsets.UTF_8));
            message.release();
            c.writeAndFlush(message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        channels.add((SocketChannel) ctx.channel());
        System.out.println(channels);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("bytebuf: " + buf);
        StringBuilder builder = new StringBuilder();
        while (buf.isReadable()) {
            builder.append((char) buf.readByte());
        }
        ctx.fireChannelRead(builder.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

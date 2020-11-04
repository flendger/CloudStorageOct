package netty.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentLinkedDeque;

public class ChatMessageHandler extends SimpleChannelInboundHandler<String> {

    static int cnt = 1;

    String id = "[user" + cnt++ + "]:";

    public static final ConcurrentLinkedDeque<ChannelHandlerContext> channels =
            new ConcurrentLinkedDeque<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
        channels.add(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        System.out.println("Message from client: " + s);
        s = id + s.replaceAll("fuck", "****");
        // ctx.writeAndFlush(s);
        String finalS = s;
        channels.forEach(c -> c.writeAndFlush(finalS));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }
}

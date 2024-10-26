package org.example.app.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        String clientName = "client-" + (NettyServer.activeConnections.size() + 1);
        ClientInfo clientInfo = new ClientInfo(clientName, LocalDateTime.now(), ctx.channel());
        NettyServer.addActiveConnection(clientInfo);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        Channel channel = ctx.channel();
        ClientInfo clientInfo = getClientInfo(channel);
        System.out.println("Message received form " + clientInfo.getName() + ": " + msg);
        if (msg.equalsIgnoreCase("exit")) {
            if (clientInfo != null) {
                System.out.println("[SERVER] " + clientInfo.getName() + " відключився");
                NettyServer.activeConnections.remove(clientInfo);
                ctx.close();
            }
        } else {
            for (ClientInfo client : NettyServer.activeConnections) {
                client.getChannel().writeAndFlush("Message from " + clientInfo.getName() + ": " + msg + '\n');
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }

    private ClientInfo getClientInfo(Channel channel) {
        for (ClientInfo client : NettyServer.activeConnections) {
            if (client.getChannel().equals(channel)) {
                return client;
            }
        }
        return null;
    }
}

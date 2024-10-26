package org.example.app.server;

import io.netty.channel.Channel;

import java.time.LocalDateTime;

public class ClientInfo {
    private final String name;
    private final LocalDateTime connectionTime;
    private final Channel channel;

    public ClientInfo(String name, LocalDateTime connectionTime, Channel channel) {
        this.name = name;
        this.connectionTime = connectionTime;
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getConnectionTime() {
        return connectionTime;
    }

    public Channel getChannel() {
        return channel;
    }
}

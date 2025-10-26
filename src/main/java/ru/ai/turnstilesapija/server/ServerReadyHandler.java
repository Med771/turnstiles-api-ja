package ru.ai.turnstilesapija.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerReadyHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final Logger logger = LoggerFactory.getLogger(ServerReadyHandler.class);

    private final NettyServer nettyServer;

    public ServerReadyHandler(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (!nettyServer.isReady()) {
            logger.warn("⛔ Server not ready — dropping message from {}", ctx.channel().remoteAddress());
            ctx.close();
            return;
        }

        ctx.fireChannelRead(msg.retain());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        if (!nettyServer.isReady()) {
            logger.warn("⛔ Server not ready — closing early connection from {}", ctx.channel().remoteAddress());
            ctx.close();
            return;
        }
        ctx.fireChannelActive();
    }
}
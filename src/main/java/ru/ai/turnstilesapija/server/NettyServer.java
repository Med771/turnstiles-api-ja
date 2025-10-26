package ru.ai.turnstilesapija.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.ai.turnstilesapija.manager.NettyRequestManager;
import ru.ai.turnstilesapija.properties.DecoderProperties;
import ru.ai.turnstilesapija.configs.TermCfg;
import ru.ai.turnstilesapija.utils.TermUtil;


@Component
@RequiredArgsConstructor
public class NettyServer {

    private EventLoopGroup boss;
    private EventLoopGroup worker;

    @Getter
    private volatile boolean ready = false;

    private final TermCfg termCfg;

    private final TermUtil termUtil;

    private final NettyRequestManager requestManager;

    private final DecoderProperties decoderProperties;

    private final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @EventListener(ApplicationReadyEvent.class)
    public void start() throws InterruptedException {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();

                        p.addLast(new ServerReadyHandler(NettyServer.this));

                        p.addLast(new DeviceInitializer(decoderProperties));
                        p.addLast(new DeviceHandler(termUtil, requestManager));
                    }
                });

        b.bind(termCfg.configPort).sync();

        ready = true;

        logger.info("✅ Netty server started on port {}", termCfg.configPort);
    }

    @PreDestroy
    public void stop() {
        ready = false;

        if (boss != null) boss.shutdownGracefully();
        if (worker != null) worker.shutdownGracefully();

        logger.info("🛑 Netty server stopped");
    }
}


package ru.ai.turnstilesapija.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.RequiredArgsConstructor;
import ru.ai.turnstilesapija.properties.DecoderProperties;

@RequiredArgsConstructor
public class DeviceInitializer extends ChannelInitializer<SocketChannel> {
    private final DecoderProperties decoderProperties;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new LengthFieldBasedFrameDecoder(
                decoderProperties.getMaxFrameLength(),   // максимум 1 MB
                decoderProperties.getLengthFieldOffset(),             // смещение длины
                decoderProperties.getLengthFieldLength(),             // размер поля длины
                decoderProperties.getLengthAdjustment(),             // без смещения поправки
                decoderProperties.getInitialBytesToStrip()             // отбросить 8 байт заголовка
        ));
    }
}
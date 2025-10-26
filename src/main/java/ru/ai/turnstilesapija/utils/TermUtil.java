package ru.ai.turnstilesapija.utils;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ai.turnstilesapija.configs.ChannelCfg;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class TermUtil {
    private final ChannelCfg channelCfg;

    public boolean registerDeviceChannel(String deviceKey, Channel channel) {
        channelCfg.getLastSeen().put(deviceKey, System.currentTimeMillis());

        if (channelCfg.getDeviceChannels().containsKey(deviceKey)) {
            return false;
        }

        channelCfg.getDeviceChannels().put(deviceKey, channel);

        return true;
    }

    public void removeChannel(Channel channel) {
        channelCfg.getDeviceChannels().entrySet().removeIf(e -> e.getValue() == channel);
    }

    public boolean isDeviceOnline(String deviceKey) {
        Channel ch = channelCfg.getDeviceChannels().get(deviceKey);
        return ch != null && ch.isActive();
    }

    public void updateLastSeen(String deviceKey) {
        channelCfg.getLastSeen().put(deviceKey, System.currentTimeMillis());
    }

    public void sendJson(Channel ch, JSONObject json) {
        byte[] body = json.toJSONString().getBytes(StandardCharsets.UTF_8);

        ByteBuf buf = Unpooled.buffer();

        buf.writeInt(0x12345678);

        buf.writeInt(body.length);

        buf.writeBytes(body);

        ch.writeAndFlush(buf);
    }

    public Channel getDeviceChannel(String deviceKey) {
        return channelCfg.getDeviceChannels().get(deviceKey);
    }
}

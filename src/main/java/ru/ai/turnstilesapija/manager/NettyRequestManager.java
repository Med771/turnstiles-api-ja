package ru.ai.turnstilesapija.manager;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ai.turnstilesapija.models.DeviceResponse;
import ru.ai.turnstilesapija.models.RequestContext;
import ru.ai.turnstilesapija.utils.TermUtil;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class NettyRequestManager {
    private final TermUtil termUtil;

    private final Map<String, RequestContext<DeviceResponse>> pending = new ConcurrentHashMap<>();

    public CompletableFuture<DeviceResponse> sendRequest(Channel channel, JSONObject json) {
        String seqId = json.getString("seqId");

        RequestContext<DeviceResponse> ctx = new RequestContext<>(seqId);
        pending.put(seqId, ctx);

        termUtil.sendJson(channel, json);

        return ctx.getFuture();
    }

    public void complete(String seqId, DeviceResponse response) {
        RequestContext<DeviceResponse> ctx = pending.remove(seqId);
        if (ctx != null) {
            ctx.getFuture().complete(response);
        }
    }
}
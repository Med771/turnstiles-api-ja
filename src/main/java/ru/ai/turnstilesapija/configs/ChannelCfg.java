package ru.ai.turnstilesapija.configs;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
@RequiredArgsConstructor
@Getter
public class ChannelCfg {
    // deviceKey -> Channel
    private final ConcurrentMap<String, Channel> deviceChannels = new ConcurrentHashMap<>();

    // seqId -> CompletableFuture for awaiting response
    private final ConcurrentMap<String, CompletableFuture<JSONObject>> pendingRequests = new ConcurrentHashMap<>();

    // last seen timestamp for devices
    private final ConcurrentMap<String, Long> lastSeen = new ConcurrentHashMap<>();
}

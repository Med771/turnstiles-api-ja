package ru.ai.turnstilesapija.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

public class RespUtil {
    public static JSONObject getLoginResp() {
        JSONObject resp = new JSONObject();

        resp.put("cmd", "login_resp");
        resp.put("code", 0);
        resp.put("message", "ok");

        return resp;
    }

    public static JSONObject getHeartBeatResp(String seqId, String deviceKey) {
        JSONObject resp = new JSONObject();

        resp.put("cmd", "heartbeat_resp");
        resp.put("seqId", seqId != null ? seqId : UUID.randomUUID().toString());
        resp.put("deviceKey", deviceKey);
        resp.put("serverTime", System.currentTimeMillis());

        return resp;
    }
}

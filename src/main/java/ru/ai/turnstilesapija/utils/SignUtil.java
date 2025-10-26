package ru.ai.turnstilesapija.utils;

import com.alibaba.fastjson.JSONObject;
import com.hfims.xcan.gateway.netty.util.EncryptUtils;
import com.hfims.xcan.gateway.netty.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SignUtil {
    public String makeSign(String deviceKey, String secret, String nonce) {

        try {
            String a = "deviceKey=" + deviceKey + "&nonce=" + nonce;
            String b = a + "&key=" + secret;

            return EncryptUtils.encryptMD5ToString(b).toLowerCase();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getDeviceConnectReq(String deviceKey, String secret, String cmd, String seqId) {
        String nonce = StringUtils.random(6, true, true);
        String sign = makeSign(deviceKey, secret, nonce);

        JSONObject req = new JSONObject();

        req.put("deviceKey", deviceKey);
        req.put("cmd", cmd);
        req.put("seqId", seqId);
        req.put("nonce", nonce);
        req.put("sign", sign);

        return req;
    }
}
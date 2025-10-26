package ru.ai.turnstilesapija.utils;


import com.alibaba.fastjson.JSONObject;
import com.hfims.xcan.gateway.netty.client.req.FaceMergeReq;
import com.hfims.xcan.gateway.netty.client.req.PersonCreateReq;
import com.hfims.xcan.gateway.netty.handler.support.Cmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ai.turnstilesapija.models.PersionPosition;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReqUtil {
    private final SignUtil signUtil;

    public JSONObject createNewPerson(String deviceKey, String secret, String name, PersionPosition position, String sn) {
        String cmd = Cmd.PERSON_CREATE_REQ.getCode();
        String seqId = UUID.randomUUID().toString();

        JSONObject req = signUtil.getDeviceConnectReq(deviceKey, secret, cmd, seqId);

        PersonCreateReq personReq = new PersonCreateReq();

        personReq.setSn(sn);
        personReq.setName(name);

        // TODO: add role

        req.put("data", personReq);

        return req;
    }

    public JSONObject mergeImageFace(String deviceKey, String secret, String sn, String imgBase64) {
        String cmd = Cmd.FACE_MERGE_REQ.getCode();
        String seqId = UUID.randomUUID().toString();

        JSONObject req = signUtil.getDeviceConnectReq(deviceKey, secret, cmd, seqId);

        FaceMergeReq faceReq = new FaceMergeReq();

        faceReq.setPersonSn(sn);
        faceReq.setImgBase64(imgBase64);

        req.put("data", faceReq);

        return req;
    }
}

package ru.ai.turnstilesapija.services;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ai.turnstilesapija.configs.TermCfg;
import ru.ai.turnstilesapija.dto.AddPersonReq;
import ru.ai.turnstilesapija.dto.AddPersonRes;
import ru.ai.turnstilesapija.manager.NettyRequestManager;
import ru.ai.turnstilesapija.models.DeviceResponse;
import ru.ai.turnstilesapija.models.PersionPosition;
import ru.ai.turnstilesapija.models.RequestContext;
import ru.ai.turnstilesapija.utils.ReqUtil;
import ru.ai.turnstilesapija.utils.TermUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class PersonServ {
    private final TermCfg termCfg;

    private final ReqUtil reqUtil;
    private final TermUtil termUtil;

    private final NettyRequestManager requestManager;

    private static final Logger logger = LoggerFactory.getLogger(PersonServ.class);

    private void addPersonInTerminal(String deviceKey, String secret, AddPersonReq addPersonReq, String sn)
            throws ExecutionException, InterruptedException, TimeoutException {

        JSONObject addPersonJSON = reqUtil.createNewPerson(deviceKey, secret, addPersonReq.name(), addPersonReq.position(), sn);
        JSONObject addFaceJSON = reqUtil.mergeImageFace(deviceKey, secret, sn, addPersonReq.baseImg64());

        Channel ch = termUtil.getDeviceChannel(deviceKey);

        CompletableFuture<DeviceResponse> addPersonFuture = requestManager.sendRequest(ch, addPersonJSON);

        DeviceResponse addPersonResp = addPersonFuture.get(10, TimeUnit.SECONDS);

        CompletableFuture<DeviceResponse> addFaceFuture = requestManager.sendRequest(ch, addFaceJSON);

        DeviceResponse addFaceResp = addFaceFuture.get(10, TimeUnit.SECONDS);
    }


    public void addPerson(AddPersonReq addPersonReq) {
        UUID sn = UUID.randomUUID();

        try {
            addPersonInTerminal(termCfg.firstDeviceSN, termCfg.firstDeviceSecret, addPersonReq, sn.toString());
            // addPersonInTerminal(termCfg.secondDeviceSN, termCfg.secondDeviceSecret, addPersonReq, sn.toString());
        } catch (Exception e) {
            logger.error("Failed to add person", e);

            throw new RuntimeException(e);
        }
    }

    public void addBatchPersons(List<AddPersonReq> addBatchPersonReq) {
        UUID taskId = UUID.randomUUID();
    }
}

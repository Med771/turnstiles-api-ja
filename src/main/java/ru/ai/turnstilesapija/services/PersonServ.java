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

    private boolean addPersonInTerminal(String deviceKey, String secret, AddPersonReq addPersonReq, String sn)
            throws ExecutionException, InterruptedException, TimeoutException {

        JSONObject addPersonJSON = reqUtil.createNewPerson(deviceKey, secret, addPersonReq.name(), addPersonReq.position(), sn);
        JSONObject addFaceJSON = reqUtil.mergeImageFace(deviceKey, secret, sn, addPersonReq.baseImg64());

        Channel ch = termUtil.getDeviceChannel(deviceKey);

        if (ch == null || !ch.isActive()) {
            return false;

            // throw new RuntimeException("Could not find channel for deviceKey: " + deviceKey);
        }

        CompletableFuture<DeviceResponse> addPersonFuture = requestManager.sendRequest(ch, addPersonJSON);

        DeviceResponse addPersonResp = addPersonFuture.get(5, TimeUnit.SECONDS);

        if (!addPersonResp.getCode().equals("000")) {
            if (addPersonResp.getCode().equals("100911")) {
                return false;
            }
            else {
                logger.info("Person add code {}, seqId {}", addPersonResp.getCode(), addPersonResp.getSeqId());

                throw new RuntimeException("Person add code " + addPersonResp.getCode() + " " + addPersonResp.getSeqId());
            }
        }

        CompletableFuture<DeviceResponse> addFaceFuture = requestManager.sendRequest(ch, addFaceJSON);

        DeviceResponse addFaceResp = addFaceFuture.get(5, TimeUnit.SECONDS);

        logger.info("Face add code {}, seqId {}", addFaceResp.getCode(), addFaceResp.getSeqId());

        return true;
    }


    public AddPersonRes addPerson(AddPersonReq addPersonReq) {
        String sn = addPersonReq.name().replaceAll(" ", "") + addPersonReq.position();

        int status = 0;

        try {
            boolean flag = addPersonInTerminal(termCfg.firstDeviceSN, termCfg.firstDeviceSecret, addPersonReq, sn);

            if (flag) {status += 1;}

        } catch (Exception e) {
            logger.error("Failed to add person on first terminal {}", e.getMessage());
        }

        try {
            boolean flag = addPersonInTerminal(termCfg.secondDeviceSN, termCfg.secondDeviceSecret, addPersonReq, sn);

            if (flag) {status += 2;}

        } catch (Exception e) {
            logger.error("Failed to add person on second terminal {}", e.getMessage());
        }

        return new AddPersonRes(status);
    }

    public void addBatchPersons(List<AddPersonReq> addBatchPersonReq) {
        UUID taskId = UUID.randomUUID();
    }
}

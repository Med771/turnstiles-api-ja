package ru.ai.turnstilesapija.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hfims.xcan.gateway.netty.handler.support.Cmd;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.ai.turnstilesapija.manager.NettyRequestManager;
import ru.ai.turnstilesapija.models.DeviceResponse;
import ru.ai.turnstilesapija.utils.RespUtil;
import ru.ai.turnstilesapija.utils.TermUtil;

@Component
@RequiredArgsConstructor
class DeviceHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final TermUtil termUtil;

    private final NettyRequestManager requestManager;

    private static final Logger logger = LoggerFactory.getLogger(DeviceHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("Channel active at {}", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        String json = msg.toString(CharsetUtil.UTF_8);
        JSONObject obj = JSON.parseObject(json);

        String cmd = obj.getString("cmd");
        String deviceKey = obj.getString("deviceKey");
        String seqId = obj.getString("seqId");

        if (deviceKey != null) {
            termUtil.updateLastSeen(deviceKey);
        }

        if (Cmd.LOGIN.getCode().equals(cmd)) {
            boolean isLogin = termUtil.registerDeviceChannel(obj.getString("deviceKey"), ctx.channel());

            if (isLogin) {
                logger.info("Login completed. SeqId: {} RemoteAddress: {}",  seqId, ctx.channel().remoteAddress());
            }

            termUtil.sendJson(ctx.channel(), RespUtil.getLoginResp());
        }

        else if (Cmd.HEARTBEAT.getCode().equals(cmd)) {
            termUtil.sendJson(ctx.channel(), RespUtil.getHeartBeatResp(seqId, deviceKey));

            logger.info("HeartBeat completed. SeqId: {} RemoteAddress: {}", seqId, ctx.channel().remoteAddress());
        }

        else if (Cmd.FACE_MERGE_RESP.getCode().equals(cmd)) {
            requestManager.complete(seqId, new DeviceResponse(cmd, seqId, obj.get("code").toString()));

            logger.info("Face merge completed. SeqId: {} RemoteAddress: {}", seqId, ctx.channel().remoteAddress());
        }

        else if (Cmd.PERSON_CREATE_RESP.getCode().equals(cmd)) {
            requestManager.complete(seqId, new DeviceResponse(cmd, seqId, obj.get("code").toString()));

            logger.info("Person create completed. SeqId: {} RemoteAddress: {}", seqId, ctx.channel().remoteAddress());
        }
        else {
            logger.info("Received cmd={} from device={} payload={}", cmd, deviceKey, obj);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        termUtil.removeChannel(ctx.channel());
        logger.info("Channel inactive at {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        logger.warn("Exception caught at {}", ctx.channel().remoteAddress(), cause);
    }
}
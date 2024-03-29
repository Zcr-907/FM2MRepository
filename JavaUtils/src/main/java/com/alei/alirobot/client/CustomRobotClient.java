package com.alei.alirobot.client;


import com.alei.alirobot.config.ConfigConstant;
import com.alei.alirobot.config.CustomRobotConfig;
import com.alei.alirobot.config.RobotConfig;
import com.alei.alirobot.okhttp.OkhttpClientUtil;
import com.alei.alirobot.request.AbstractRequest;
import com.alei.alirobot.request.enums.MsgType;
import com.alei.alirobot.request.enums.UseMsgType;
import com.alei.alirobot.utils.RobotUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author LeiLiMin
 * @Description: 自定义机器人客户端
 * @date: 2022/6/15
 */
@Slf4j
public class CustomRobotClient extends AbstractRobotClient<CustomRobotConfig> {

    public CustomRobotClient(OkhttpClientUtil jackpotOkHttpClient) {
        super(jackpotOkHttpClient);
    }

    /**
     * 自定义机器人: 检测配置
     */
    @Override
    public void checkByConfig(CustomRobotConfig config, AbstractRequest request) {
        // 关键词检测 - 默认不开启
    }

    /**
     * 请求Header参数设置
     */
    @Override
    Map<String, String> settingHeaderParamByConfig(CustomRobotConfig config) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        HashMap<String, String> headerParamMap = new HashMap<>();
        long currentMills = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();

        // 1.加签信息
        headerParamMap.put(ConfigConstant.ACCESS_TOKEN, config.getAccessToken());

        // 2.时间戳
        headerParamMap.put(ConfigConstant.TIMESTAMP, String.valueOf(currentMills));

        // 3.数字签名
        if (!StringUtils.isBlank(config.getSecret())) {
            headerParamMap.put(ConfigConstant.SIGN, RobotUtils.calculateSign(config.getSecret(), currentMills));
        }
        return headerParamMap;
    }

    @Override
    void sendBasicMsg(CustomRobotConfig config, AbstractRequest request) {
        try {
            this.checkByConfig(config, request);

            Map<String, String> paramMap = this.settingHeaderParamByConfig(config);

            Response post = this.getJackpotOkHttpClient().post(config.getWebhook(), paramMap, request.putInfo());
            log.debug("[CustomRobotClient sendBasicMsg] sendMsg success,post[{}]", post);
        } catch (NoSuchAlgorithmException | InvalidKeyException | IllegalAccessException e) {
            log.error("[CustomRobotClient sendBasicMsg] produce msg fail");
        } catch (Exception e) {
            log.error("[CustomRobotClient sendBasicMsg] http request fail");
        }
    }

    @Override
    public void sendMsg(RobotConfig config, AbstractRequest request) {
        // 1.获取消息类型
        String requestType = request.getRealUseMsgType();

        // 2.根据消息类型选择消息发送方式
        MsgType requestMsgType = UseMsgType.getMsgTypeByInUseMsgType(requestType);
        if (Objects.equals(requestMsgType, MsgType.BASIC_MSG)) {
            sendBasicMsg((CustomRobotConfig) config, request);
        } else if (Objects.equals(requestMsgType, MsgType.DEFAULT_NONE_MSG)) {
            log.error("[CustomRobotClient sendMsg] send fail because msgType not found[{}]", request);
        }
    }
}

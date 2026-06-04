package com.warehouse.check.mq;

import com.warehouse.common.constant.MqConstants;
import com.warehouse.common.dto.StockUpdateMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class CheckMqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendCheckAdjust(StockUpdateMessage message) {
        String destination = MqConstants.TOPIC_STOCK_UPDATE + ":" + MqConstants.TAG_STOCK_CHECK;
        rocketMQTemplate.syncSend(destination, MessageBuilder.withPayload(message).build());
        log.info("发送盘点调整MQ: {}", message);
    }
}

package com.warehouse.inout.mq;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.warehouse.common.constant.MqConstants;
import com.warehouse.common.dto.StockUpdateMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StockMqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @SentinelResource(value = "sendStockUpdateMq",fallback = "sendMqFallback")
    public void sendStockUpdate(StockUpdateMessage message, String tag) {
        String destination = MqConstants.TOPIC_STOCK_UPDATE + ":" + tag;
        rocketMQTemplate.syncSend(destination, MessageBuilder.withPayload(message).build());
        log.info("发送库存MQ消息 destination={}, message={}", destination, message);
    }
    public void sendMqFallback(StockUpdateMessage message,String tag,Throwable ex){
        log.error("MQ发送失败降级，消息{}，异常{}",message,ex.getMessage());
    }
}

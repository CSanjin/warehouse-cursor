package com.warehouse.stock.mq;

import com.warehouse.common.constant.MqConstants;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.stock.service.StockRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = MqConstants.TOPIC_STOCK_UPDATE,
        consumerGroup = MqConstants.CONSUMER_GROUP_STOCK,
        selectorExpression = MqConstants.TAG_STOCK_IN + " || " + MqConstants.TAG_STOCK_OUT + " || " + MqConstants.TAG_STOCK_CHECK
)
public class StockUpdateConsumer implements RocketMQListener<StockUpdateMessage> {

    @Resource
    private StockRecordService stockRecordService;

    @Override
    public void onMessage(StockUpdateMessage message) {
        log.info("收到库存更新消息: {}", message);
        stockRecordService.handleStockUpdate(message);
    }
}

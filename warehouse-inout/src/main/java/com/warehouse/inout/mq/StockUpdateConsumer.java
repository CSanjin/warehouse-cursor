package com.warehouse.inout.mq;

import com.alibaba.fastjson.JSON;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.inout.feign.StockFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Slf4j
@Service
@RocketMQMessageListener(
        topic = "stock_update_topic",
        consumerGroup = "warehouse-inout-consumer-group",
        selectorExpression = "*",
        messageModel = MessageModel.CLUSTERING,
        consumeMode = ConsumeMode.CONCURRENTLY,
        maxReconsumeTimes = 3
)
public class StockUpdateConsumer implements RocketMQListener<String> {

    @Resource
    private StockFeignClient stockFeignClient;

    @Override
    public void onMessage(String msgJson) {
        log.info("消费库存消息:{}",msgJson);
        StockUpdateMessage msg = JSON.parseObject(msgJson,StockUpdateMessage.class);
        //远程调用库存服务
        Boolean result = stockFeignClient.updateStock(msg.getGoodsId(), msg.getQuantity());
        log.info("库存更新结果:{},单号:{}",result,msg.getBizNo());
    }
}
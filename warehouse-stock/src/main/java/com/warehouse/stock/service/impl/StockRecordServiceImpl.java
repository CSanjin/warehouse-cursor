package com.warehouse.stock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.common.exception.BusinessException;
import com.warehouse.stock.entity.StockRecord;
import com.warehouse.stock.mapper.StockRecordMapper;
import com.warehouse.stock.service.StockRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockRecordServiceImpl extends ServiceImpl<StockRecordMapper, StockRecord> implements StockRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleStockUpdate(StockUpdateMessage message) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockRecord::getWarehouseId, message.getWarehouseId())
                .eq(StockRecord::getGoodsId, message.getGoodsId());
        StockRecord record = getOne(wrapper);
        String bizType = message.getBizType();
        int qty = message.getQuantity() == null ? 0 : message.getQuantity();

        if ("IN".equals(bizType)) {
            if (record == null) {
                record = new StockRecord();
                record.setWarehouseId(message.getWarehouseId());
                record.setGoodsId(message.getGoodsId());
                record.setQuantity(qty);
                record.setLockQuantity(0);
                record.setRemark("MQ入库初始化");
                save(record);
            } else {
                record.setQuantity(record.getQuantity() + qty);
                updateById(record);
            }
        } else if ("OUT".equals(bizType)) {
            if (record == null || record.getQuantity() < qty) {
                throw new BusinessException("库存不足，无法出库");
            }
            record.setQuantity(record.getQuantity() - qty);
            updateById(record);
        } else if ("CHECK".equals(bizType)) {
            if (record == null) {
                record = new StockRecord();
                record.setWarehouseId(message.getWarehouseId());
                record.setGoodsId(message.getGoodsId());
                record.setQuantity(qty);
                record.setLockQuantity(0);
                record.setRemark("盘点调整");
                save(record);
            } else {
                record.setQuantity(record.getQuantity() + qty);
                updateById(record);
            }
        } else {
            throw new BusinessException("未知库存业务类型: " + bizType);
        }
    }
}

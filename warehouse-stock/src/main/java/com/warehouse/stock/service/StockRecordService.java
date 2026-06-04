package com.warehouse.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.stock.entity.StockRecord;

public interface StockRecordService extends IService<StockRecord> {

    void handleStockUpdate(StockUpdateMessage message);
}

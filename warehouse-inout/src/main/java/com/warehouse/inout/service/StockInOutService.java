package com.warehouse.inout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warehouse.inout.entity.InoutRecord;
import com.warehouse.inout.entity.StockInOut;

public interface StockInOutService extends IService<StockInOut> {

    boolean confirm(Long id);
    Boolean addRecord(InoutRecord record);
}

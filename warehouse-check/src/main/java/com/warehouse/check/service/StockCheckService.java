package com.warehouse.check.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.warehouse.check.entity.StockCheck;

public interface StockCheckService extends IService<StockCheck> {

    boolean confirm(Long id);
}

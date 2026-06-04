package com.warehouse.base.api;

import com.warehouse.base.entity.WarehouseInfo;
import com.warehouse.common.feign.FeignFallbackFactory;
import com.warehouse.common.result.Result;
import org.springframework.stereotype.Component;

@Component
public class WarehouseFeignFallbackFactory extends FeignFallbackFactory<WarehouseFeignApi> {

    @Override
    public WarehouseFeignApi create(Throwable cause) {
        logError("warehouse-base", cause);
        return id -> degradeResult();
    }
}

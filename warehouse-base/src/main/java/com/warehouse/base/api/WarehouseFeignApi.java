package com.warehouse.base.api;

import com.warehouse.base.entity.WarehouseInfo;
import com.warehouse.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "warehouse-base", contextId = "warehouseFeignApi", fallbackFactory = WarehouseFeignFallbackFactory.class)
public interface WarehouseFeignApi {

    @GetMapping("/api/base/warehouses/{id}")
    Result<WarehouseInfo> getById(@PathVariable("id") Long id);
}

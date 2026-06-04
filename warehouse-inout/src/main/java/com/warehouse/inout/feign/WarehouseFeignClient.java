package com.warehouse.inout.feign;

import com.warehouse.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "warehouse-base", contextId = "inoutWarehouseFeign", fallbackFactory = WarehouseFeignClientFallbackFactory.class)
public interface WarehouseFeignClient {

    @GetMapping("/api/base/warehouses/{id}")
    Result<Map<String, Object>> getById(@PathVariable("id") Long id);
}

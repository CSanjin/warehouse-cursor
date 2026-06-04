package com.warehouse.inout.feign;

import com.warehouse.common.feign.FeignFallbackFactory;
import com.warehouse.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WarehouseFeignClientFallbackFactory extends FeignFallbackFactory<WarehouseFeignClient> {

    @Override
    public WarehouseFeignClient create(Throwable cause) {
        logError("warehouse-base", cause);
        return id -> degradeResult();
    }
}

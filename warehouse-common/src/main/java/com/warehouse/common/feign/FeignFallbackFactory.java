package com.warehouse.common.feign;

import com.warehouse.common.result.Result;
import com.warehouse.common.result.ResultCode;
import org.springframework.cloud.openfeign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class FeignFallbackFactory<T> implements FallbackFactory<T> {

    protected <R> Result<R> degradeResult() {
        return Result.fail(ResultCode.DEGRADE.getCode(), ResultCode.DEGRADE.getMessage());
    }

    protected void logError(String serviceName, Throwable cause) {
        log.error("{} Feign调用失败: {}", serviceName, cause.getMessage());
    }
}

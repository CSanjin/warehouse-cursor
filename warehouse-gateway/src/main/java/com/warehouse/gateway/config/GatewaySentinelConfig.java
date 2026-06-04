package com.warehouse.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.common.result.Result;
import com.warehouse.common.result.ResultCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Configuration
public class GatewaySentinelConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        BlockRequestHandler blockRequestHandler = (ServerWebExchange exchange, Throwable ex) -> {
            Result<Void> result = Result.fail(ResultCode.FLOW_LIMIT.getCode(), ResultCode.FLOW_LIMIT.getMessage());
            try {
                String body = objectMapper.writeValueAsString(result);
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body);
            } catch (JsonProcessingException e) {
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }
}

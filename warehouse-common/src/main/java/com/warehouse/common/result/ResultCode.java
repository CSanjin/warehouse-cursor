package com.warehouse.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    FLOW_LIMIT(429, "请求过于频繁，请稍后再试"),
    DEGRADE(503, "服务降级，请稍后再试"),
    BUSINESS_ERROR(600, "业务异常");

    private final Integer code;
    private final String message;
}

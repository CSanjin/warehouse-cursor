package com.warehouse.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockUpdateMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long warehouseId;
    private Long goodsId;
    private Integer quantity;
    private String bizType;
    private String bizNo;
    private String operator;

}

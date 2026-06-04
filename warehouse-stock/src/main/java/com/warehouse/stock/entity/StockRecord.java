package com.warehouse.stock.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_record")
public class StockRecord extends BaseEntity {

    private Long warehouseId;
    private Long goodsId;
    private Integer quantity;
    private Integer lockQuantity;
    private String remark;
}

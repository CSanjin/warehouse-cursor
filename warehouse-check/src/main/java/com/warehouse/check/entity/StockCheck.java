package com.warehouse.check.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_check")
public class StockCheck extends BaseEntity {

    private String checkNo;
    private Long warehouseId;
    private Long goodsId;
    private Integer bookQuantity;
    private Integer actualQuantity;
    private Integer diffQuantity;
    private String operator;
    /** 0待确认 1已确认 */
    private Integer status;
    private String remark;
}

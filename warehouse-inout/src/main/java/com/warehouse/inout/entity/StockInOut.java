package com.warehouse.inout.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stock_inout")
public class StockInOut extends BaseEntity {

    private String orderNo;
    /** IN入库 OUT出库 */
    private String orderType;
    private Long warehouseId;
    private Long goodsId;
    private Integer quantity;
    private String operator;
    /** 0待确认 1已确认 2已取消 */
    private Integer status;
    private String remark;
}

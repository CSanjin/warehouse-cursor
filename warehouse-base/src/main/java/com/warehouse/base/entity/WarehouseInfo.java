package com.warehouse.base.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_info")
public class WarehouseInfo extends BaseEntity {

    private String warehouseCode;
    private String warehouseName;
    private String address;
    private String manager;
    private String phone;
    /** 0停用 1启用 */
    private Integer status;
}

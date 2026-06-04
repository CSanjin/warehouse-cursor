package com.warehouse.goods.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.warehouse.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("goods")
public class Goods extends BaseEntity {

    private String goodsCode;
    private String goodsName;
    private String category;
    private String unit;
    private BigDecimal price;
    private String spec;
    /** 0下架 1上架 */
    private Integer status;
}

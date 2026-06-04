package com.warehouse.inout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("inout_record")
public class InoutRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long goodsId;
    private Integer inoutNum;
    private String orderNo;
}
package com.warehouse.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.base.entity.WarehouseInfo;
import com.warehouse.base.mapper.WarehouseInfoMapper;
import com.warehouse.base.service.WarehouseInfoService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseInfoServiceImpl extends ServiceImpl<WarehouseInfoMapper, WarehouseInfo> implements WarehouseInfoService {
}

package com.warehouse.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import com.warehouse.base.entity.WarehouseInfo;
import com.warehouse.base.service.WarehouseInfoService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/base/warehouses")
public class WarehouseInfoController {

    @Resource
    private WarehouseInfoService warehouseInfoService;

    @GetMapping("/page")
    public Result<PageResult<WarehouseInfo>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String warehouseName) {
        LambdaQueryWrapper<WarehouseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(warehouseName), WarehouseInfo::getWarehouseName, warehouseName);
        Page<WarehouseInfo> page = warehouseInfoService.page(new Page<>(current, size), wrapper);
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<WarehouseInfo> getById(@PathVariable Long id) {
        return Result.ok(warehouseInfoService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody WarehouseInfo warehouse) {
        return Result.ok(warehouseInfoService.save(warehouse));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody WarehouseInfo warehouse) {
        return Result.ok(warehouseInfoService.updateById(warehouse));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(warehouseInfoService.removeById(id));
    }
}

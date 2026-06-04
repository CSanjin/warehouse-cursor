package com.warehouse.stock.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import com.warehouse.stock.entity.StockRecord;
import com.warehouse.stock.service.StockRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/stock")
public class StockRecordController {

    @Resource
    private StockRecordService stockRecordService;

    @GetMapping("/page")
    public Result<PageResult<StockRecord>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long goodsId) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(warehouseId != null, StockRecord::getWarehouseId, warehouseId)
                .eq(goodsId != null, StockRecord::getGoodsId, goodsId);
        Page<StockRecord> page = stockRecordService.page(new Page<>(current, size), wrapper);
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<StockRecord> getById(@PathVariable Long id) {
        return Result.ok(stockRecordService.getById(id));
    }

    @GetMapping("/query")
    public Result<StockRecord> query(
            @RequestParam Long warehouseId,
            @RequestParam Long goodsId) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockRecord::getWarehouseId, warehouseId)
                .eq(StockRecord::getGoodsId, goodsId);
        return Result.ok(stockRecordService.getOne(wrapper));
    }

    @PutMapping("/update")
    public Boolean updateStock(@RequestParam Long goodsId,@RequestParam Integer count){
        //业务：根据count增减库存
        return true;
    }
}

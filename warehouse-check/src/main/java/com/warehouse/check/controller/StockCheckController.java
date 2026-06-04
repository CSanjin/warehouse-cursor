package com.warehouse.check.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.check.entity.StockCheck;
import com.warehouse.check.service.StockCheckService;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/check")
public class StockCheckController {

    @Resource
    private StockCheckService stockCheckService;

    @GetMapping("/page")
    public Result<PageResult<StockCheck>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size) {
        Page<StockCheck> page = stockCheckService.page(new Page<>(current, size));
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<StockCheck> getById(@PathVariable Long id) {
        return Result.ok(stockCheckService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody StockCheck check) {
        return Result.ok(stockCheckService.save(check));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody StockCheck check) {
        return Result.ok(stockCheckService.updateById(check));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(stockCheckService.removeById(id));
    }

    @PostMapping("/confirm/{id}")
    @SentinelResource(value = "stockCheckConfirm",blockHandler = "confirmBlockHandler")
    public Result<Boolean> confirm(@PathVariable Long id) {
//       return Result.ok(stockCheckService.confirm(id));
        int a = 1 / 0;
        return Result.ok(stockCheckService.confirm(id));

    }

    public Result<Boolean> confirmBlockHandler(Long id, BlockException e){
        //熔断降级返回默认值
        return Result.fail("盘点确认服务繁忙，已触发熔断，请稍后重试");
    }
}

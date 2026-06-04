package com.warehouse.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import com.warehouse.goods.entity.Goods;
import com.warehouse.goods.service.GoodsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @GetMapping("/page")
    public Result<PageResult<Goods>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String goodsName) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(goodsName), Goods::getGoodsName, goodsName);
        Page<Goods> page = goodsService.page(new Page<>(current, size), wrapper);
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<Goods> getById(@PathVariable Long id) {
        return Result.ok(goodsService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody Goods goods) {
        return Result.ok(goodsService.save(goods));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody Goods goods) {
        return Result.ok(goodsService.updateById(goods));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(goodsService.removeById(id));
    }
}

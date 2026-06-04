package com.warehouse.inout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.common.result.PageResult;
import com.warehouse.common.result.Result;
import com.warehouse.inout.entity.InoutRecord;
import com.warehouse.inout.entity.StockInOut;
import com.warehouse.inout.mq.StockMqProducer;
import com.warehouse.inout.service.StockInOutService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/inout")
public class StockInOutController {

    @Resource
    private StockInOutService stockInOutService;

    @Resource
    private StockMqProducer stockMqProducer;



    @GetMapping("/page")
    public Result<PageResult<StockInOut>> page(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String orderType) {
        LambdaQueryWrapper<StockInOut> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(orderType), StockInOut::getOrderType, orderType);
        Page<StockInOut> page = stockInOutService.page(new Page<>(current, size), wrapper);
        return Result.ok(PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<StockInOut> getById(@PathVariable Long id) {
        return Result.ok(stockInOutService.getById(id));
    }

    @PostMapping
    public Result<Boolean> save(@RequestBody StockInOut order) {
        return Result.ok(stockInOutService.save(order));
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody StockInOut order) {
        return Result.ok(stockInOutService.updateById(order));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ok(stockInOutService.removeById(id));
    }

    @PostMapping("/confirm/{id}")
    public Result<Boolean> confirm(@PathVariable Long id) {
        return Result.ok(stockInOutService.confirm(id));
    }

    @GetMapping("/sendMq")
    public String sendMq(@RequestParam Long goodsId,
                         @RequestParam Integer count,
                         @RequestParam String orderNo,
                         @RequestParam String tag){
        StockUpdateMessage msg = new StockUpdateMessage();
        msg.setGoodsId(goodsId);
        msg.setQuantity(count);
        msg.setBizNo(orderNo);
        msg.setWarehouseId(1L);
        msg.setBizType(tag);
        stockMqProducer.sendStockUpdate(msg,tag);
        return "发送成功";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long goodsId,@RequestParam Integer num,@RequestParam String orderNo){
        InoutRecord record = new InoutRecord();
        record.setGoodsId(goodsId);
        record.setInoutNum(num);
        record.setOrderNo(orderNo);
        Boolean res = stockInOutService.addRecord(record);
        return res?"成功":"失败";
    }
}

package com.warehouse.stock.controller;
import com.warehouse.stock.service.StockService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/stock", method = {RequestMethod.POST, RequestMethod.PUT})
public class StockController {
    @Resource
    private StockService stockService;

    @PutMapping("/update")
    public Boolean update(@RequestParam Long goodsId,@RequestParam Integer num){
        return stockService.updateStock(goodsId,num);
    }
}
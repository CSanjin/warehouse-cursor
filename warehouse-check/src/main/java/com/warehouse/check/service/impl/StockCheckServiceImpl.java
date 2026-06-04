package com.warehouse.check.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.check.entity.StockCheck;
import com.warehouse.check.feign.StockFeignClient;
import com.warehouse.check.mapper.StockCheckMapper;
import com.warehouse.check.mq.CheckMqProducer;
import com.warehouse.check.service.StockCheckService;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.common.exception.BusinessException;
import com.warehouse.common.result.Result;
import com.warehouse.common.result.ResultCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class StockCheckServiceImpl extends ServiceImpl<StockCheckMapper, StockCheck> implements StockCheckService {

    @Resource
    private StockFeignClient stockFeignClient;
    @Resource
    private CheckMqProducer checkMqProducer;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(Long id) {
        StockCheck check = getById(id);
        if (check == null) {
            throw new BusinessException("盘点单不存在");
        }
        if (check.getStatus() != null && check.getStatus() == 1) {
            throw new BusinessException("盘点单已确认");
        }

        int bookQty = 0;
        Result<Map<String, Object>> stockResult = stockFeignClient.query(check.getWarehouseId(), check.getGoodsId());
        if (stockResult != null && ResultCode.SUCCESS.getCode().equals(stockResult.getCode()) && stockResult.getData() != null) {
            Object qty = stockResult.getData().get("quantity");
            if (qty instanceof Number) {
                bookQty = ((Number) qty).intValue();
            }
        }
        check.setBookQuantity(bookQty);
        int diff = check.getActualQuantity() - bookQty;
        check.setDiffQuantity(diff);
        check.setStatus(1);

        if (diff != 0) {
            StockUpdateMessage message = new StockUpdateMessage();
            message.setWarehouseId(check.getWarehouseId());
            message.setGoodsId(check.getGoodsId());
            message.setQuantity(diff);
            message.setBizType("CHECK");
            message.setBizNo(check.getCheckNo());
            message.setOperator(check.getOperator());
            checkMqProducer.sendCheckAdjust(message);
        }
        return updateById(check);
    }

    @Override
    public boolean save(StockCheck entity) {
        if (entity.getCheckNo() == null) {
            entity.setCheckNo("CK" + IdUtil.getSnowflakeNextIdStr());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        return super.save(entity);
    }
}

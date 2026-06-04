package com.warehouse.inout.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.warehouse.common.constant.MqConstants;
import com.warehouse.common.dto.StockUpdateMessage;
import com.warehouse.common.exception.BusinessException;
import com.warehouse.common.result.Result;
import com.warehouse.common.result.ResultCode;
import com.warehouse.inout.entity.InoutRecord;
import com.warehouse.inout.entity.StockInOut;
import com.warehouse.inout.feign.GoodsFeignClient;
import com.warehouse.inout.feign.StockFeignClient;
import com.warehouse.inout.feign.WarehouseFeignClient;
import com.warehouse.inout.mapper.InoutRecordMapper;
import com.warehouse.inout.mapper.StockInOutMapper;
import com.warehouse.inout.mq.StockMqProducer;
import com.warehouse.inout.service.StockInOutService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class StockInOutServiceImpl extends ServiceImpl<StockInOutMapper, StockInOut> implements StockInOutService {

    @Resource
    private GoodsFeignClient goodsFeignClient;
    @Resource
    private WarehouseFeignClient warehouseFeignClient;
    @Resource
    private StockMqProducer stockMqProducer;
    @Resource
    private InoutRecordMapper mapper;
    @Resource
    private StockFeignClient stockFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(Long id) {
        StockInOut order = getById(id);
        if (order == null) {
            throw new BusinessException("出入库单不存在");
        }
        if (order.getStatus() != null && order.getStatus() == 1) {
            throw new BusinessException("单据已确认，请勿重复操作");
        }

        Result<Map<String, Object>> goodsResult = goodsFeignClient.getById(order.getGoodsId());
        if (goodsResult == null || !ResultCode.SUCCESS.getCode().equals(goodsResult.getCode())) {
            throw new BusinessException("商品服务不可用或商品不存在");
        }
        Result<Map<String, Object>> warehouseResult = warehouseFeignClient.getById(order.getWarehouseId());
        if (warehouseResult == null || !ResultCode.SUCCESS.getCode().equals(warehouseResult.getCode())) {
            throw new BusinessException("仓库服务不可用或仓库不存在");
        }

        StockUpdateMessage message = new StockUpdateMessage();
        message.setWarehouseId(order.getWarehouseId());
        message.setGoodsId(order.getGoodsId());
        message.setQuantity(order.getQuantity());
        message.setBizNo(order.getOrderNo());
        message.setOperator(order.getOperator());

        if ("IN".equals(order.getOrderType())) {
            message.setBizType("IN");
            stockMqProducer.sendStockUpdate(message, MqConstants.TAG_STOCK_IN);
        } else if ("OUT".equals(order.getOrderType())) {
            message.setBizType("OUT");
            stockMqProducer.sendStockUpdate(message, MqConstants.TAG_STOCK_OUT);
        } else {
            throw new BusinessException("未知单据类型");
        }

        order.setStatus(1);
        return updateById(order);
    }

    @Override
    public boolean save(StockInOut entity) {
        if (entity.getOrderNo() == null) {
            entity.setOrderNo("IO" + IdUtil.getSnowflakeNextIdStr());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        return super.save(entity);
    }

    //全局分布式事务注解，出现异常两边全部回滚
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public Boolean addRecord(InoutRecord record) {
        //1.本地入库落库
        mapper.insert(record);
        //2.远程调用stock修改库存
        stockFeignClient.updateStock(record.getGoodsId(),record.getInoutNum());

        //测试回滚：放开代码，模拟异常
//        int i = 1/0;
        return true;
    }
}

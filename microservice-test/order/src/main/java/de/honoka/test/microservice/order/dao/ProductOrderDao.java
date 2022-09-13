package de.honoka.test.microservice.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import de.honoka.test.microservice.order.entity.ProductOrder;

public interface ProductOrderDao extends BaseMapper<ProductOrder> {

    void clear();
}
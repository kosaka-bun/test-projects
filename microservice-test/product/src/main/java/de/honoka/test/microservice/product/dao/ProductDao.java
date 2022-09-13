package de.honoka.test.microservice.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import de.honoka.test.microservice.product.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao extends BaseMapper<Product> {

    void clear();

    List<Product> findByName(@Param("name") String name);

    Product selectForUpdate(@Param("id") int productId);

    void setCount(@Param("id") int productId, @Param("count") int count);
}

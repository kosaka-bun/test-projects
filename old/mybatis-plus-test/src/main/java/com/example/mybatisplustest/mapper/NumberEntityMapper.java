package com.example.mybatisplustest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatisplustest.entity.NumberEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.math.BigInteger;

@Mapper
public interface NumberEntityMapper extends BaseMapper<NumberEntity> {

    void update(@Param("id") Long id,
                @Param("integer") BigInteger integer,
                @Param("decimal") BigDecimal decimal);
}

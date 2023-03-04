package com.example.mybatisplustest.controller;

import com.example.mybatisplustest.entity.NumberEntity;
import com.example.mybatisplustest.mapper.NumberEntityMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/number_entity")
public class NumberEntityController {

    @Resource
    private NumberEntityMapper numberEntityMapper;

    @GetMapping("/all")
    public List<NumberEntity> findAll() {
        return numberEntityMapper.selectList(null);
    }

    @PostMapping
    public NumberEntity insert(BigInteger integer, BigDecimal decimal) {
        NumberEntity numberEntity = new NumberEntity()
                .setIntegerCol(integer)
                .setDecimalCol(decimal);
        numberEntityMapper.insert(numberEntity);
        return numberEntityMapper.selectById(numberEntity.getId());
    }

    @PutMapping("/{id}")
    public NumberEntity update(@PathVariable Long id,
                               BigInteger integer,
                               BigDecimal decimal) {
        numberEntityMapper.update(id, integer, decimal);
        return numberEntityMapper.selectById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        numberEntityMapper.deleteById(id);
    }
}

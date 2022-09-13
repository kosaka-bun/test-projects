package com.example.mybatisplustest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatisplustest.entity.TestUser;

import java.util.List;

public interface TestUserMapper extends BaseMapper<TestUser> {

    List<Integer> findAllNumCol1();
}
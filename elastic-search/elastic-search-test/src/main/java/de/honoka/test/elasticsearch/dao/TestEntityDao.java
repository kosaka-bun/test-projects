package de.honoka.test.elasticsearch.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import de.honoka.test.elasticsearch.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestEntityDao extends BaseMapper<TestEntity> {

    void deleteAll();
}
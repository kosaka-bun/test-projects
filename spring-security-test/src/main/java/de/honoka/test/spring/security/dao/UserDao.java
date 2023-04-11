package de.honoka.test.spring.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import de.honoka.test.spring.security.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {

}

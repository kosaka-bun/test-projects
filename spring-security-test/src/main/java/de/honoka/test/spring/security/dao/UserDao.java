package de.honoka.test.spring.security.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import de.honoka.test.spring.security.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao extends BaseMapper<User> {

    User findByUsername(@Param("username") String username);
}

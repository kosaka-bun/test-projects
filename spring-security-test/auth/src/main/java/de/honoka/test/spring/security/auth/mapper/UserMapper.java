package de.honoka.test.spring.security.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import de.honoka.test.spring.security.auth.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User findByUsername(@Param("username") String username);
}

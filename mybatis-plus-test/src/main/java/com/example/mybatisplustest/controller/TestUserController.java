package com.example.mybatisplustest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mybatisplustest.entity.TestUser;
import com.example.mybatisplustest.mapper.TestUserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class TestUserController {

    @Resource
    private TestUserMapper testUserMapper;

    @GetMapping("/all")
    public Object findAll() {
        return testUserMapper.selectList(null);
    }

    @PostMapping
    public void insert(Integer num) {
        Random random = new Random();
        for(int i = 0; i < num; i++) {
            testUserMapper.insert(new TestUser()
                    .setName(UUID.randomUUID().toString().substring(0, 8))
                    .setJoinTime(new Date(System.currentTimeMillis() +
                            random.nextInt(10000 * 1000) - 5000 * 1000))
                    .setNumCol1(random.nextInt(10) + 1)
                    .setNumCol2(random.nextInt(10) + 1)
                    .setValid(random.nextBoolean()));
        }
    }

    @PostMapping("/{id}")
    public void insertOne(@PathVariable Long id) {
        Random random = new Random();
        try {
            testUserMapper.insert(new TestUser().setId(id)
                    .setName(UUID.randomUUID().toString().substring(0, 8))
                    .setJoinTime(new Date(System.currentTimeMillis() +
                            random.nextInt(10000 * 1000) - 5000 * 1000))
                    .setNumCol1(random.nextInt(10) + 1)
                    .setNumCol2(random.nextInt(10) + 1)
                    .setValid(random.nextBoolean()));
        } catch(Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

    @DeleteMapping("/all")
    public void clear() {
        testUserMapper.delete(null);
    }

    @GetMapping
    public Object find(TestUser param) {
        return testUserMapper.selectList(new QueryWrapper<>(param));
    }

    @GetMapping("/scope")
    public Object findByScope(Integer numCol1, Integer numCol2) {
        LambdaQueryWrapper<TestUser> wrapper = new LambdaQueryWrapper<>();
        if(numCol1 != null) wrapper.le(TestUser::getNumCol1, numCol1);
        if(numCol2 != null) wrapper.gt(TestUser::getNumCol2, numCol2);
        return testUserMapper.selectList(wrapper);
    }

    @GetMapping("/null")
    public Object findByNull() {
        LambdaQueryWrapper<TestUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(TestUser::getValid);
        return testUserMapper.selectList(wrapper);
    }

    @GetMapping("/page/{page}")
    public Object findByPage(@PathVariable Integer page,
            @RequestParam(name = "ps", required = false, defaultValue = "5")
            Integer pageSize) {
        Page<TestUser> pageObj = testUserMapper.selectPage(
                new Page<>(page, pageSize),
                new LambdaQueryWrapper<TestUser>()
                        .orderByDesc(TestUser::getJoinTime));
        return pageObj;
    }

    @GetMapping("/num_col1/all")
    public List<Integer> findAllNumCol1() {
        return testUserMapper.findAllNumCol1();
    }
}

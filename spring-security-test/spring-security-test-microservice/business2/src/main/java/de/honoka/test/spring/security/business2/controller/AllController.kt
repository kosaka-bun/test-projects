package de.honoka.test.spring.security.business2.controller

import cn.hutool.json.JSONObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AllController {

    @GetMapping("/res1")
    fun res1(): JSONObject = JSONObject().set("hello", "business2")
}
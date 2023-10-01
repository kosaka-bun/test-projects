package de.honoka.test.spring.transacational.controller

import de.honoka.test.spring.transacational.service.TestService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
        val testService: TestService
) {

    @RequestMapping("/test1")
    fun test1() {
        testService.service1()
    }

    @RequestMapping("/test2")
    fun test2() {
        testService.service2()
    }

    @RequestMapping("/test3")
    fun test3() {
        testService.service3()
    }
}

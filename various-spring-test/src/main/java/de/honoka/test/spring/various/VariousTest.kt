package de.honoka.test.spring.various

import cn.hutool.json.JSONObject
import de.honoka.sdk.util.framework.web.WebUtils.clientRealIp
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/various")
@RestController
class VariousTest {

    @GetMapping("/test1")
    fun test1(request: HttpServletRequest): Any? {
        val header = JSONObject()
        request.headerNames.asIterator().forEach {
            header[it] = request.getHeaders(it).toList()
        }
        return header
    }

    @GetMapping("/test2")
    fun test2(request: HttpServletRequest): Any? = request.clientRealIp
}
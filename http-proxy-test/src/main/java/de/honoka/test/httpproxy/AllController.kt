package de.honoka.test.httpproxy

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
class AllController {

    @RequestMapping("/**")
    fun test1(request: HttpServletRequest) {
        println("${request.method} ${request.requestURI}?${request.queryString}")
        println("Headers:")
        for(headerName in request.headerNames) {
            println("$headerName: ${request.getHeader(headerName)}")
        }
        println("---".repeat(10))
        println("Body:")
        println(request.inputStream.reader().readText())
        println("---".repeat(10))
    }
}
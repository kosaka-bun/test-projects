package de.honoka.test.spring.security.business1.util

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Suppress("unused")
class Oauth2FeignInterceptor : RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val attributes = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes? ?: return
        template.header(HttpHeaders.AUTHORIZATION, attributes.request.getHeader(HttpHeaders.AUTHORIZATION))
    }
}

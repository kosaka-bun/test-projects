package de.honoka.test.spring.security.security

import de.honoka.sdk.json.api.JsonObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationEntryPointImpl : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        response.outputStream.writer(Charsets.UTF_8).use {
            it.write(JsonObject.of().add("msg", "没有权限访问，请登录").toString())
        }
    }
}
package de.honoka.test.spring.security.security

import de.honoka.sdk.json.api.JsonObject
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AccessDeniedHandlerImpl : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpStatus.FORBIDDEN.value()
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        response.outputStream.writer(Charsets.UTF_8).use {
            it.write(JsonObject.of().add("msg", "访问被拒绝").toString())
        }
    }
}
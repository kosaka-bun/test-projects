package de.honoka.test.spring.security.auth.component

import cn.hutool.core.exceptions.ExceptionUtil
import de.honoka.sdk.util.framework.web.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {

        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
    
    @ExceptionHandler
    fun handle(t: Throwable, request: HttpServletRequest, response: HttpServletResponse): ApiResponse<*> {
        log.error("", t)
        response.status = when(t) {
            is NoResourceFoundException -> HttpStatus.NOT_FOUND.value()
            else -> HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
        val msg = if(t.message?.isNotBlank() == true) {
            t.message
        } else {
            ExceptionUtil.getMessage(t)
        }
        return ApiResponse.fail(msg)
    }
    
    @ExceptionHandler
    fun handle(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ApiResponse<*> {
        val message = e.allErrors.map { it.defaultMessage }.joinToString()
        return handle(IllegalArgumentException(message), request, response)
    }
}

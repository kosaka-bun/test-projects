package de.honoka.test.spring.security.auth.security

import cn.hutool.json.JSONObject
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler

/*
 * Spring Security所自动配置的FilterChain中存在着ExceptionTranslationFilter与AuthorizationFilter，
 * 其中AuthorizationFilter会在ExceptionTranslationFilter之后执行，ExceptionTranslationFilter会捕获
 * 在其之后执行的Filter中所抛出的各类异常。
 * ExceptionTranslationFilter中存在accessDeniedHandler和authenticationEntryPoint两个字段，在
 * ExceptionTranslationFilter捕获到AccessDeniedException时，会根据SecurityContextHolder的context
 * 中的authentication的信息的情况，来决定调用这两个字段中的哪一个，以对请求和响应进行后续处理。
 * 一个请求对应一个线程，一个线程对应一个SecurityContextHolder，可在代码的任何地方通过SecurityContextHolder
 * 获取到线程对应的Security Context。
 * 这两个字段可在开发者针对HttpSecurity进行exceptionHandling配置的时候，引入自定义的实现。Spring会将
 * 配置中所指定的自定义实现赋值给ExceptionTranslationFilter中的这两个字段。
 */

/*
 * AuthenticationEntryPoint的本意为认证入口点，即Spring的本意时在调用方未登录时，能够通过AuthenticationEntryPoint
 * 来主动跳转到登录页，或返回提示信息引导用户进行登录。
 */
/**
 * 当ExceptionTranslationFilter之后存在Filter抛出AccessDeniedException时，ExceptionTranslationFilter
 * 会检查SecurityContextHolder的context中是否存在authentication信息，若不存在，则视为请求方未登录，调用
 * 本类中的方法对请求和响应进行处理。
 * 此处为返回一段JSON提示信息。
 */
object AuthenticationEntryPointImpl : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException?
    ) = response.run {
        status = HttpStatus.UNAUTHORIZED.value()
        addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        outputStream.writer(Charsets.UTF_8).use { os ->
            var msg = "没有权限访问，请登录"
            if(authException is BadCredentialsException) {
                msg = authException.message!!
            }
            os.write(JSONObject().set("msg", msg).toString())
        }
    }
}

/*
 * 当SecurityContextHolder的context中的authentication对象不为空时，以下情况会使AuthorizationFilter抛出
 * AccessDeniedException：
 * 1. authentication对象的authenticated字段为false。
 * 2. 请求的接口路径被设置为anonymous，但authentication对象不是AnonymousAuthenticationToken类型。
 */
/**
 * 当ExceptionTranslationFilter之后存在Filter抛出AccessDeniedException时，ExceptionTranslationFilter
 * 会检查SecurityContextHolder的context中是否存在authentication信息，若存在，则视为请求方已登录但无权访问
 * 指定的路径，调用本类中的方法对请求和响应进行处理。
 * 此处为返回一段JSON提示信息。
 */
object AccessDeniedHandlerImpl : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) = response.run {
        status = HttpStatus.FORBIDDEN.value()
        addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        outputStream.writer(Charsets.UTF_8).use {
            it.write(JSONObject().set("msg", "访问被拒绝").toString())
        }
    }
}

package de.honoka.test.spring.security.security

import de.honoka.test.spring.security.dao.UserDao
import de.honoka.test.spring.security.entity.User
import de.honoka.test.spring.security.util.Utils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoginFilter(
    private val userDao: UserDao
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = Utils.getTokenByAuthorization(
            request.getHeader("Authorization")
        )
        /*
         * 如果没有token，那么应当放行，因为只要SecurityContextHolder的
         * context中没有authentication，那么后续的过滤器就不会放行
         *
         * 但如果这里不放行的话，配置为默认放行的那些资源，就会直接在这里被拦截
         */
        token ?: run {
            filterChain.doFilter(request, response)
            return
        }
        if(!LoginCache.tokenUserMap.containsKey(token)) {
            throw RuntimeException("token无效")
        }
        //把token对应的用户信息，转换为authentication，存入SecurityContextHolder上下文当中
        //注意：这里必须得使用三个参数的authentication
        //第三个参数为授权，也就是用户是啥身份
        val authentication = UsernamePasswordAuthenticationToken(
            LoginCache.tokenUserMap[token],
            null,
            null
        )
        SecurityContextHolder.getContext().authentication = authentication
        //执行之后的过滤器
        //仅当SecurityContextHolder的context中有authentication时，后续过滤器才会放行
        //一个请求对应一个线程，一个线程对应一个SecurityContextHolder
        filterChain.doFilter(request, response)
    }
}

object LoginCache {

    val tokenUserMap = HashMap<String, User>()
}
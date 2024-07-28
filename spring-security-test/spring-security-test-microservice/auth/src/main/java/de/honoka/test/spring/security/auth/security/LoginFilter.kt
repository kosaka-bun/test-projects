package de.honoka.test.spring.security.auth.security

import de.honoka.test.spring.security.auth.entity.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

/**
 * 在存在登录态的情况下，为SecurityContextHolder的context添加authentication信息
 */
object LoginFilter : OncePerRequestFilter() {

    val tokenUserMap = HashMap<String, User>()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader("X-Token")
        if(token?.isBlank() == true || !tokenUserMap.containsKey(token)) {
            filterChain.doFilter(request, response)
            return
        }
        val authentication = UsernamePasswordAuthenticationToken(tokenUserMap[token], null)
        SecurityContextHolder.getContext().authentication = authentication
        filterChain.doFilter(request, response)
    }
}
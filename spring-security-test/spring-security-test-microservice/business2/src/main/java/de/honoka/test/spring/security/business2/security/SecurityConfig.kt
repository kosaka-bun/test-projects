package de.honoka.test.spring.security.business2.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http.run {
        authorizeHttpRequests {
            it.anyRequest().authenticated()
        }
        oauth2ResourceServer {
            it.jwt(Customizer.withDefaults())
        }
        build()
    }
}
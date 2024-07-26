package de.honoka.test.spring.security.auth.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@EnableWebSecurity
@Configuration
class SecurityConfig {

    /*
     * 当@Bean注解与@Order注解连用时，表示为来自同一个类的Bean实例赋予在其他Bean中被注入的顺序。
     * 例如，此处创建了一个SecurityFilterChain，@Order的值为1，则当另一个Bean通过@Resource或构造器参数等方式注入
     * List<SecurityFilterChain>类型的字段或构造器参数时，此处创建的SecurityFilterChain会排在List中的第一个。
     */
    /**
     * OAuth2与OpenID Connect（OIDC）的相关配置
     */
    @Order(1)
    @Bean
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http.run {
        //OAuth2AuthorizationServerConfiguration是针对OAuth2的默认@Configuration类，通过此方法为http对象进行默认配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(this)
        getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).run {
            //启用OpenID Connect
            oidc(Customizer.withDefaults())
        }
        exceptionHandling {
            //将需要认证的请求，重定向到login进行登录认证
            it.defaultAuthenticationEntryPointFor(
                LoginUrlAuthenticationEntryPoint("/login"),
                MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
        }
        oauth2ResourceServer {
            //使用JWT处理接收到的Access Token
            it.jwt(Customizer.withDefaults())
        }
        build()
    }

    @Order(2)
    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http.run {
        authorizeHttpRequests {
            //设置所有请求都需要认证，未认证的请求都被重定向到login页面进行登录
            it.anyRequest().authenticated()
        }
        //由Spring Security过滤链中UsernamePasswordAuthenticationFilter过滤器拦截处理login页面提交的登录信息
        formLogin(Customizer.withDefaults())
        build()
    }

    //查询认证服务器信息：http://localhost:8081/.well-known/openid-configuration
    /**
     * 通过手动组装OAuth客户端的方式，创建一个基于内存的RegisteredClientRepository
     */
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val oidcClient = RegisteredClient.withId(UUID.randomUUID().toString()).run {
            clientId("spring-security-test-gateway")
            //{noop}开头，表示以明文存储后面的密码
            clientSecret("{noop}123456")
            //请求授权码时使用默认的认证方式
            clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            //配置授权码模式，刷新令牌，客户端模式
            authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            //用户确认授权后，请求以下回调地址，并在请求参数中携带code参数（授权码）
            redirectUri("https://www.baidu.com")
            //用户在认证服务中登出后，需要跳转到的地址
            postLogoutRedirectUri("https://www.sogou.com")
            //设置客户端权限范围
            scope(OidcScopes.OPENID)
            scope(OidcScopes.PROFILE)
            clientSettings(ClientSettings.builder().run {
                //客户端在请求认证服务获取授权码时，需要由用户在网页上进行授权
                requireAuthorizationConsent(true)
                build()
            })
            build()
        }
        //配置基于内存的客户端信息
        return InMemoryRegisteredClientRepository(oidcClient)
    }

    //JWK相关资料：https://datatracker.ietf.org/doc/html/draft-ietf-jose-json-web-key-41
    /**
     * 配置JWK，为JWT提供加密密钥，用于加密、解密或签名、验签
     */
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyPair = KeyPairGenerator.getInstance("RSA").run {
            initialize(2048)
            generateKeyPair()
        }
        val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey).apply {
            privateKey(keyPair.private as RSAPrivateKey)
            keyID(UUID.randomUUID().toString())
        }.build()
        return ImmutableJWKSet(JWKSet(rsaKey))
    }

    /**
     * 配置JWT解析器
     */
    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder = run {
        OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    /**
     * 配置授权服务器请求地址（与OAuth2相关的一些请求地址，默认为/oauth2/token等）
     */
    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings = AuthorizationServerSettings.builder().run {
        //不作配置，使用默认地址
        build()
    }
}
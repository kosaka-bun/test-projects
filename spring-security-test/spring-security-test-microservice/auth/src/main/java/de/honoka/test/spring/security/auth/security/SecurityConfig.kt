package de.honoka.test.spring.security.auth.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.SecurityContextHolderFilter
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@EnableWebSecurity
@Configuration
class SecurityConfig {

    companion object {

        val passwordEncoder = BCryptPasswordEncoder()
    }

    /**
     * OAuth2的相关配置
     */
    @Bean
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http.run {
        /*
         * OAuth2AuthorizationServerConfiguration是针对OAuth2的默认@Configuration类，通过此方法为
         * http对象进行默认配置。
         * 默认配置中有一个名为endpointsMatcher的局部变量，其包含了项目中定义的AuthorizationServerSettings
         * 所配置的OAuth2相关的几个接口的路径，如/oauth2/token等。
         * endpointsMatcher在默认配置中被传递给了securityMatcher方法，该方法用于设置http对象所包含的配置
         * 要作用于哪些请求路径。即此处表示该http对象所包含的配置仅作用于OAuth2相关的几个接口。
         * 同时，endpointsMatcher也传递给了csrf配置的ignoringRequestMatchers方法，不对这些接口进行csrf防护。
         */
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(this)
        //添加能够识别自定义登录态，并将其放入SecurityContextHolder中的处理器
        //不可用OAuth2相关Filter来确定要添加的Filter所处的位置，因为OAuth2相关Filter在调用build时才会被添加
        addFilterAfter(CustomLoginStatusFilter, SecurityContextHolderFilter::class.java)
        getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).run {
            authorizationEndpoint {
                /*
                 * 设置自定义授权确认页面路径（可以为任意路径，通常推荐为/oauth2/consent）
                 *
                 * 此路径必须设置，否则Spring Security会在客户端请求/oauth2/authorize时直接返回一段html，
                 * 难以获取state等参数。
                 * /oauth2/consent这个路径本身不存在，理论上是由开发者自行实现，但也可以不实现，而是在调用
                 * /oauth2/authorize得到301响应后，直接根据要重定向的URL，拿到其中的state值。
                 */
                it.consentPage("/oauth2/consent")
            }
        }
        oauth2ResourceServer {
            //使用JWT处理接收到的Access Token
            it.jwt(Customizer.withDefaults())
        }
        //设置自定义的Security异常处理器，用于处理未登录、无权访问等情况
        exceptionHandling {
            /*
             * 定义认证入口点，当AuthorizationFilter检测到SecurityContextHolder的context中没有
             * authentication信息时，则调用此处配置的authenticationEntryPoint中的方法，来执行开发者
             * 定义的后续行为。
             */
            it.authenticationEntryPoint(AuthenticationEntryPointImpl)
            it.accessDeniedHandler(AccessDeniedHandlerImpl)
        }
        build()
    }

    //查询认证服务器信息：http://localhost:8081/.well-known/oauth-authorization-server
    /**
     * 通过手动组装OAuth2客户端的方式，创建一个基于内存的RegisteredClientRepository
     */
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val gatewayClient = RegisteredClient.withId(UUID.randomUUID().toString()).run {
            val id = "oauth2-client1"
            clientId(id)
            clientSecret(passwordEncoder.encode("123456"))
            /*
             * 通过/oauth2/token接口提供授权码以获取token时，使用默认的认证方式（用于校验是哪个用户在为指定的
             * 第三方应用请求token）。
             * Basic：指的是在Authorization请求头中使用“Basic {base64}”的方式请求授权码，其中base64的内容为
             * “{clientId}:{clientSecret（明文）}”的base64编码（不含大括号）。
             * Post：指的是在POST请求体中附带client_id与client_secret（明文）两个参数。
             */
            clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            //配置授权码模式，刷新令牌，客户端模式
            authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            //用户确认授权后，请求以下回调地址，并在请求参数中携带code参数（授权码）
            redirectUri("http://localhost:8084/login/oauth2/code/$id")
            redirectUri("https://www.baidu.com")
            //设置客户端权限范围
            scope("all")
            clientSettings(ClientSettings.builder().run {
                /*
                 * 客户端在请求认证服务获取授权码时，需要先重定向到用于确认授权的路径（既可能是网页也可能是
                 * 接口）。Spring在返回重定向响应时，除了用于确认授权的路径，还会额外附带三个参数：
                 * client_id：OAuth2客户端名
                 * state：用于在通过POST方式请求/oauth2/authorize验证请求合法性（确保在请求之前打开过用于
                 * 确认授权的路径）
                 * scope：可供用户选择的访问内容，空格隔开
                 */
                requireAuthorizationConsent(true)
                build()
            })
            build()
        }
        //配置基于内存的客户端信息
        return InMemoryRegisteredClientRepository(gatewayClient)
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

    @Bean
    fun passwordEncoder(): PasswordEncoder = passwordEncoder
}

package de.honoka.test.spring.security.auth.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
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
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.security.web.context.SecurityContextHolderFilter
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import kotlin.apply
import kotlin.apply as kApply

@EnableWebSecurity
@Configuration
class SecurityConfig {

    companion object {

        val passwordEncoder = BCryptPasswordEncoder()
    }

    /*
     * 当@Bean注解与@Order注解连用时，表示为来自同一个类的Bean实例赋予在其他Bean中被注入的顺序。
     * 例如，此处创建了一个SecurityFilterChain，@Order的值为2，则当另一个Bean通过@Resource或构造器参数等方式注入
     * List<SecurityFilterChain>类型的字段或构造器参数时，此处创建的SecurityFilterChain会排在List中的第二个（根据
     * Order注解的值进行排序，排第二位，而不是因为注解值为2，从而被放在List中的第二位）。
     */
    /*
     * Spring Boot 3.0以上所使用的Spring Security在进行配置时，与之前版本有所不同，可以通过在配置类中
     * 定义多个SecurityFilterChain的方式，来实现针对不同接口的配置。
     * 用于定义SecurityFilterChain的方法，需要传入HttpSecurity，并在代码中对HttpSecurity进行配置，
     * HttpSecurity可以通过build方法生成SecurityFilterChain。
     * 每个SecurityFilterChain代表一套独立的配置，可以通过在HttpSecurity中调用securityMatcher方法，
     * 来指定这套配置要作用于哪些URL路径。
     * 若存在多个SecurityFilterChain可同时对一些路径生效，可通过@Order注解来指定它们的优先级，任何一个
     * 请求都只能被一个SecurityFilterChain处理。
     * Spring会根据配置类中定义的每个SecurityFilterChain的具体配置，为它们各自生成一个FilterChainProxy
     * 和包含在FilterChainProxy中的一套Filter。SecurityFilterChain互相之间不共用FilterChainProxy和其中
     * 的Filter，它们各自拥有的FilterChainProxy和其中的Filter，是完全根据各自的配置，各自生成的一套对象。
     * 每个SecurityFilterChain所拥有的FilterChainProxy和一套Filter均与其他SecurityFilterChain所拥有的
     * 这些对象各不相同（Filter的数量、顺序、类型、hashCode等均有不同），完全独立。
     */
    /**
     * Spring Security的路径拦截、跳转与放行规则等配置
     */
    @Order(2)
    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http.kApply {
        val permitList = arrayOf(
            //允许注册
            "/user/info",
            /*
             * 在不设置ExceptionHandler的情况下，若接口逻辑中抛出异常，Spring会默认将请求转发到/error路径。
             * 若不设置放行/error，则在访问白名单接口且白名单接口抛出异常时，不会返回任何与接口逻辑有关的错误
             * 信息，而是会跳转到登录页，或者错误地返回403状态码（取决于配置）。
             */
            "/error"
        )
        val anonymousList = arrayOf(
            "/user/login"
        )
        /*
         * 在这里配置的拦截与放行规则会在AuthorizationFilter中被使用，该过滤器在默认情况下为FilterChain中
         * 优先级最低的过滤器。
         */
        authorizeHttpRequests {
            it.requestMatchers(*permitList).permitAll()
            //设置仅允许在未登录的情况下访问的接口
            it.requestMatchers(*anonymousList).anonymous()
            //设置其他所有请求都需要认证
            it.anyRequest().authenticated()
        }
        csrf {
            /*
             * 若不在此处对白名单中的URL表达式进行忽略，仅在authorizeHttpRequests中设置permitAll的话，
             * CSRF拦截器会导致白名单中的URL不可由GET以外的方式去访问。
             * 例如，若有一个接口/test被设置为白名单，其同时支持通过GET与POST进行请求，则若不在此处设置
             * 忽略，则仅在通过GET方式请求/test接口时能够正常访问，即使在authorizeHttpRequests中对该接
             * 口设置了白名单，其他方式的请求依旧会被拦截。
             */
            it.ignoringRequestMatchers(*permitList, *anonymousList)
        }
        //添加能够识别自定义登录态，并将其放入SecurityContextHolder中的处理器
        addFilterBefore(CustomLoginStatusFilter, AuthorizationFilter::class.java)
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
        /*
         * 未登录时，跳转到Spring Security自带的登录页面进行登录。
         * formLogin方法用于使http应用FormLoginConfigurer，以添加DefaultLoginPageGeneratingFilter，
         * 该拦截器会在请求方访问/login路径时直接向请求方返回登录页面的HTML内容作为响应，并不再执行后面
         * 的AuthorizationFilter以校验请求方是否有/login路径的访问权限，因此可以认为调用formLogin方法
         * 默认放行了/login路径。
         * 若在exceptionHandling中配置了authenticationEntryPoint，则formLogin不会生效。
         */
        //formLogin(Customizer.withDefaults())
    }.build()

    /**
     * OAuth2的相关配置
     */
    @Order(1)
    @Bean
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain = http.kApply {
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
        getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).run {
            authorizationEndpoint {
                //设置自定义授权确认页面路径
                it.consentPage("/oauth2/consent")
            }
        }
        addFilterAfter(CustomLoginStatusFilter, SecurityContextHolderFilter::class.java)
        exceptionHandling {
            it.authenticationEntryPoint(AuthenticationEntryPointImpl)
            it.accessDeniedHandler(AccessDeniedHandlerImpl)
        }
        oauth2ResourceServer {
            //使用JWT处理接收到的Access Token
            it.jwt(Customizer.withDefaults())
        }
    }.build()

    //查询认证服务器信息：http://localhost:8081/.well-known/openid-configuration
    /**
     * 通过手动组装OAuth2客户端的方式，创建一个基于内存的RegisteredClientRepository
     */
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val gatewayClient = RegisteredClient.withId(UUID.randomUUID().toString()).apply {
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
            clientSettings(ClientSettings.builder().apply {
                /*
                 * 客户端在请求认证服务获取授权码时，需要先重定向到用于确认授权的路径（既可能是网页也可能是
                 * 接口）。Spring在返回重定向响应时，除了用于确认授权的路径，还会额外附带三个参数：
                 * client_id：OAuth2客户端名
                 * state：用于在通过POST方式请求/oauth2/authorize验证请求合法性（确保在请求之前打开过用于
                 * 确认授权的路径）
                 * scope：可供用户选择的访问内容，空格隔开
                 */
                requireAuthorizationConsent(true)
            }.build())
        }.build()
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
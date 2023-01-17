package de.honoka.test.spring.transacational.service

import de.honoka.sdk.util.code.ColorfulText
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TestService {

    @Transactional
    fun service1() {
        ColorfulText.of().green("service1").println()
    }

    @Transactional
    fun service2() {
        ColorfulText.of().green("service2").println()
        throw RuntimeException()
    }

    fun service3() {
        service1()
        service2()
    }
}

package de.honoka.test.spring.crt.test1

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class Bean1 : ApplicationRunner {

    @Resource
    private var bean2: Bean2? = null

    override fun run(args: ApplicationArguments) {
        println("Bean1")
        println(bean2)
    }

    override fun toString(): String {
        return "Bean1: bean2=" + (bean2?.hashCode() ?: "null")
    }
}

@Component
class Bean2 : ApplicationRunner {

    @Lazy
    @Resource
    private var bean1: Bean1? = null

    override fun run(args: ApplicationArguments) {
        println("Bean2")
        println(bean1)
    }

    override fun toString(): String {
        return "Bean2: bean1=" + (bean1?.hashCode() ?: "null")
    }
}
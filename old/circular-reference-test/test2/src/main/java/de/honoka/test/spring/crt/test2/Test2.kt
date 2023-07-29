package de.honoka.test.spring.crt.test2

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class MessengerFramework {

    @Resource
    private lateinit var listener: MessageListener

    fun send(str: String) {
        println("发送：$str")
    }

    fun receive(str: String) {
        listener.onMessage(str)
    }
}

@Component
class MessageListener {

    @Resource
    private lateinit var framework: MessengerFramework

    fun onMessage(str: String) {
        println("收到：$str")
        framework.send("长度：${str.length}")
    }
}

@Component
class Test2 : ApplicationRunner {

    @Resource
    private lateinit var framework: MessengerFramework

    override fun run(args: ApplicationArguments) {
        framework.receive("hello")
    }
}
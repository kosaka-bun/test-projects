package de.honoka.test.spring.crt.test2

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct
import javax.annotation.Resource

@Component
class MessengerFramework {

    private val listeners: MutableList<MessageListener> = arrayListOf()

    fun send(str: String) {
        println("发送：$str")
    }

    fun receive(str: String) {
        listeners.forEach {
            it.onMessage(str)
        }
    }

    fun registerListener(listener: MessageListener) {
        listeners.add(listener)
    }
}

@Component
class MessageListener {

    @Resource
    private lateinit var framework: MessengerFramework

    @PostConstruct
    fun register() {
        framework.registerListener(this)
    }

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
package de.honoka.test.spring.security

import de.honoka.sdk.util.code.ThrowsRunnable
import de.honoka.sdk.util.framework.spring.gui.SpringBootConsoleWindow
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.util.function.Consumer

@SpringBootApplication
class SpringSecurityTestApplication

fun main(args: Array<String>) {
    SpringBootConsoleWindow.of(1.25, SpringSecurityTestApplication::class.java).run {
        applicationArgs = args
        beforeRunApplication = ThrowsRunnable {
            System.err.println("before")
        }
        onExit = Consumer {
            System.err.println(it)
        }
        configureWindowBuilder {
            it.textPaneFontName = "宋体"
        }
        createAndRun()
    }
}
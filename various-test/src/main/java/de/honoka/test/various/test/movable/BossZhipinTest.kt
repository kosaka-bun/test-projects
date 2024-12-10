package de.honoka.test.various.test.movable

import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.devtools.Connection
import org.openqa.selenium.devtools.DevTools
import org.openqa.selenium.devtools.v85.network.Network
import org.openqa.selenium.devtools.v85.network.model.ResponseReceived
import java.awt.Toolkit
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
object BossZhipinTest {
    
    val executor = ScheduledThreadPoolExecutor(1)
    
    lateinit var task: ScheduledFuture<*>
    
    lateinit var driver: ChromeDriver
    
    lateinit var cookie: String
    
    @JvmStatic
    fun main(args: Array<String>) {
        val action = {
            try {
                action()
            } catch(t: Throwable) {
                t.printStackTrace()
            }
        }
        task = executor.scheduleWithFixedDelay(action, 0, 5, TimeUnit.SECONDS)
        TimeUnit.HOURS.sleep(1)
    }
    
    fun action() {
        disableLog()
        initCookie()
        exitProcess(0)
    }
    
    fun initCookie() {
        val url = "https://www.zhipin.com/web/geek/job?query=Java&city=101270100&page=1"
        val userDataDir = Path("./build/selenium/user-data").absolute().normalize()
        driver = ChromeDriver(ChromeOptions().apply {
            addArguments("--user-data-dir=$userDataDir")
            //addArguments("--headless")
        })
        driver.run {
            devTools.run {
                createSession()
                send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))
                addListener(Network.responseReceived()) { e ->
                    runCatching {
                        handleResponse(this, e)
                    }
                }
            }
            get(url)
            for(i in 1..10) {
                TimeUnit.SECONDS.sleep(1)
                cookie = executeScript("return document.cookie") as String
                if(cookie.contains("__zp_stoken__")) break
            }
            println(cookie)
            TimeUnit.SECONDS.sleep(10)
            quit()
        }
    }
    
    fun ChromeDriver.moveToCenter() = manage().window().run {
        size = Dimension(1280, 850)
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val left = (screenSize.width - size.width) / 2
        val top = (screenSize.height - size.height) / 2
        position = Point(left, top)
    }
    
    fun disableLog() {
        val classes = listOf(
            DevTools::class.java,
            Connection::class.java
        )
        classes.forEach {
            val logger = it.getDeclaredField("LOG").run {
                isAccessible = true
                get(null) as Logger
            }
            logger.level = Level.OFF
        }
        Thread.setDefaultUncaughtExceptionHandler { _, _ -> }
    }
    
    fun handleResponse(devTools: DevTools, event: ResponseReceived) {
        val url = event.response.url
        if(url.startsWith("https://www.zhipin.com/wapi/zpgeek/search/joblist.json")) {
            println(url)
            val responseBody = devTools.send(Network.getResponseBody(event.requestId))
            println(responseBody.body)
        }
    }
}
package de.honoka.test.various.test.movable

import cn.hutool.core.io.FileUtil
import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.WebDriver.Window
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.awt.Toolkit
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.absolute

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
        initCookie()
        println(cookie)
    }
    
    fun initCookie() {
        val url = "https://www.zhipin.com/web/geek/job?query=Java&city=101270100&page=1"
        val userDataDir = Path("./build/selenium/user-data").absolute().normalize()
        driver = ChromeDriver(ChromeOptions().apply {
            FileUtil.del(userDataDir)
            FileUtil.mkdir(userDataDir)
            addArguments("--user-data-dir=$userDataDir")
            addArguments("--start-minimized")
        })
        driver.run {
            manage().window().run {
                size = Dimension(1280, 850)
                moveToCenter()
            }
            get(url)
            for(i in 1..10) {
                TimeUnit.SECONDS.sleep(1)
                cookie = executeScript("return document.cookie") as String
                if(cookie.contains("__zp_stoken__")) break
            }
            quit()
            FileUtil.del(userDataDir)
        }
    }
    
    fun Window.moveToCenter() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val left = (screenSize.width - size.width) / 2
        val top = (screenSize.height - size.height) / 2
        position = Point(left, top)
    }
}
package de.honoka.test.various.test.movable

import de.honoka.sdk.util.kotlin.net.proxy.ProxyPool
import de.honoka.test.various.test.movable.BossZhipinTest.moveToCenter
import org.openqa.selenium.By
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.ByteArrayOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.io.path.absolute
import kotlin.system.exitProcess

@Suppress("MemberVisibilityCanBePrivate")
object ProxyTest {
    
    lateinit var driver: ChromeDriver
    
    @JvmStatic
    fun main(args: Array<String>) {
        runCatching {
            action()
        }
        TimeUnit.MINUTES.sleep(1)
        exitProcess(0)
    }
    
    fun action() {
        BossZhipinTest.disableLog()
        val userDataDir = Path("./build/selenium/user-data").absolute().normalize()
        driver = ChromeDriver(ChromeOptions().apply {
            //ProxyPool.init()
            val proxy = Proxy().apply {
                //httpProxy = ProxyPool.randomProxy
                httpProxy = "127.0.0.1:10000"
                sslProxy = httpProxy
                println("Proxy: $httpProxy")
            }
            setProxy(proxy)
            addArguments("--user-data-dir=$userDataDir")
            //addArguments("--headless")
        })
        driver.run {
            moveToCenter()
            printIp()
            quit()
        }
    }
    
    fun printIp() {
        driver.get("https://httpbin.org/ip")
        println(driver.findElement(By.tagName("body")).text)
    }
    
    fun changeProxy() {
        ProxyPool.init()
        val proxy = Proxy().apply {
            httpProxy = ProxyPool.randomProxy
            sslProxy = httpProxy
            println("Proxy: $httpProxy")
        }
        driver.capabilities.merge(ChromeOptions().apply {
            setProxy(proxy)
        })
    }
}

@Suppress("MemberVisibilityCanBePrivate")
object ProxyServerTest {
    
    val serverSocket: ServerSocket = ServerSocket(10000)
    
    @JvmStatic
    fun main(args: Array<String>) {
        println(serverSocket)
        while(true) {
            val connection = serverSocket.accept()
            Thread {
                runCatching {
                    connection.use { handle(it) }
                }.exceptionOrNull()?.printStackTrace()
            }.start()
        }
    }
    
    fun handle(connection: Socket) {
        val os = ByteArrayOutputStream()
        runCatching {
            while(true) {
                val buffer = ByteArray(1024)
                val read = connection.getInputStream().read(buffer)
                if(read > 0) os.write(buffer, 0, read)
                if(read < 0) break
            }
        }
        println(String(os.toByteArray()))
    }
}

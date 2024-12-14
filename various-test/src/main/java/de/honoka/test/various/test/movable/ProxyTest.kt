package de.honoka.test.various.test.movable

import cn.hutool.http.HttpUtil
import de.honoka.sdk.util.kotlin.code.repeatRunCatching
import de.honoka.sdk.util.kotlin.code.tryBlock
import de.honoka.sdk.util.kotlin.net.HttpUtilExt
import de.honoka.sdk.util.kotlin.net.browserApiHeaders
import de.honoka.sdk.util.kotlin.text.forEachWrapper
import de.honoka.sdk.util.kotlin.text.toJsonWrapper
import org.jsoup.Jsoup
import java.lang.annotation.Inherited
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.hasAnnotation

object ProxyTest {
    
    @JvmStatic
    fun main(args: Array<String>) {
        ProxySources.all.forEach {
            it().forEach { s ->
                val proxy = s.split(":")
                HttpUtil.createGet("http://httpbin.org/ip").run {
                    println(s)
                    browserApiHeaders()
                    setHttpProxy(proxy[0], proxy[1].toInt())
                    timeout(TimeUnit.SECONDS.toMillis(5).toInt())
                    runCatching {
                        val res = execute()
                        println(res.status)
                        println(res.body())
                    }
                    println("\n")
                }
            }
        }
    }
}

@Suppress("unused")
object ProxySources {
    
    @Inherited
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class ProxySource(val name: String, val order: Double = Double.MAX_VALUE)
    
    @Suppress("UNCHECKED_CAST")
    val all: List<() -> List<String>> = run {
        val funList = this::class.declaredFunctions.filter { it.hasAnnotation<ProxySource>() }
        funList.sortedBy { it.findAnnotations(ProxySource::class).first().order }.map {
            { it.call(this) as List<String> }
        }
    }
    
    @ProxySource("docip", 1.0)
    fun docip(): List<String> {
        val url = "https://www.docip.net/data/free.json"
        val list = ArrayList<String>()
        val res = tryBlock(3) { HttpUtil.get(url) }.toJsonWrapper()
        res.getArray("data").forEachWrapper {
            list.add(it.getStr("ip"))
        }
        return list
    }
    
    @ProxySource("zdaye", 2.0)
    fun zdaye(): List<String> {
        fun url(page: Int) = "https://www.zdaye.com/free/$page/?checktime=5&sleep=2"
        return parseFromTable(::url, "#ipc tbody")
    }
    
    @ProxySource("ip3366", 3.0)
    fun ip3366(): List<String> {
        fun url(page: Int) = "http://www.ip3366.net/free/?stype=1&page=$page"
        return parseFromTable(::url, "#list tbody")
    }
    
    @ProxySource("89ip", 4.0)
    fun the89ip(): List<String> {
        fun url(page: Int) = "https://www.89ip.cn/index_$page.html"
        return parseFromTable(::url, "div.fly-panel tbody")
    }
    
    @ProxySource("kuaidaili", 5.0)
    fun kuaidaili(): List<String> {
        fun url(page: Int) = "https://www.kuaidaili.com/free/inha/$page/"
        return parseFromTable(::url, "#table__free-proxy tbody")
    }
    
    private inline fun parseFromTable(
        urlTemplate: (Int) -> String,
        cssSelector: String,
        ipIndex: Int = 0,
        portIndex: Int = 1
    ): List<String> {
        val list = ArrayList<String>()
        repeatRunCatching(5) { i ->
            val doc = tryBlock(3) {
                val res = HttpUtilExt.getWithBrowserHeaders(urlTemplate(i + 1))
                Jsoup.parse(res)
            }
            val table = doc.expectFirst(cssSelector)
            table.getElementsByTag("tr").forEach {
                val tdList = it.getElementsByTag("td")
                list.add("${tdList[ipIndex].text().trim()}:${tdList[portIndex].text().trim()}")
            }
        }
        return list
    }
}

package de.honoka.test.spring.item.groovy

import groovy.lang.GroovyClassLoader
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/groovy/classLoader")
@RestController
class GroovyClassLoaderTest {

    val classLoader = GroovyClassLoader(javaClass.classLoader)

    private fun invoke(fileName: String, methodName: String = "fun1"): String {
        val classStr = javaClass.getResource("/item/groovy-test/$fileName.groovy")?.readText()
        val clazz = classLoader.parseClass(classStr)
        return clazz.getDeclaredMethod(methodName).invoke(null) as String
    }

    @GetMapping("/test1")
    fun test1(): String = invoke("Class1")

    @GetMapping("/test2")
    fun test2(): String = invoke("Class2")

    @GetMapping("/test3")
    fun test3(): String = invoke("Class1Changed")

    @GetMapping("/test4")
    fun test4(): String {
        val className = "de.honoka.test.spring.item.groovy.dynamic.Class1"
        val clazz = GroovyClassLoader(this.classLoader).loadClass(className)
        return clazz.getDeclaredMethod("fun1").invoke(null) as String
    }
}

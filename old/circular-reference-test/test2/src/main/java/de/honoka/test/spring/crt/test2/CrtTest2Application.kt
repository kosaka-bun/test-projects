package de.honoka.test.spring.crt.test2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

//region ComponentScan
//另一种在Spring容器中排除指定Component的方式
@ComponentScan(excludeFilters = [
    //ComponentScan.Filter(
    //    type = FilterType.ASSIGNABLE_TYPE,
    //    classes = [TestBean::class]
    //)
    ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ["de.honoka.test.spring.crt.test2.TestBean"]
    )
])
//endregion
@SpringBootApplication
class CrtTest2Application

fun main(args: Array<String>) {
    runApplication<CrtTest2Application>(*args)
}
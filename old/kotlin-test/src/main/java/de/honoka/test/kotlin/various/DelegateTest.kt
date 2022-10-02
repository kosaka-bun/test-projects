package de.honoka.test.kotlin.various

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

object DelegateTest {

    class Base {

        private var field1 = "value of base field1"

        operator fun getValue(sub: Sub, property: KProperty<*>): Any {
            if(property.name == "f1") return field1
            return Unit
        }

        operator fun setValue(sub: Sub, property: KProperty<*>, any: Any) {
            if(property.name == "f1") field1 = any as String
        }
    }

    class Sub {

        private val base = Base()

        var f1 by base

        var f2 by base

        val f3 by lazy {
            println("lazy value is accessed")
            "lazy"
        }

        var f4 by Delegates.observable("field4") {
            prop, old, new ->
            println("${prop.name} has been changed to $new from $old")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val sub = Sub()
        //f1
        println(sub.f1)
        sub.f1 = "changed"
        println(sub.f1)
        //f2
        println(sub.f2)
        sub.f2 = "changed"
        println(sub.f2)
        //f3
        Thread.sleep(2000)
        println(sub.f3)
        println(sub.f3)
        //f4
        println(sub.f4)
        sub.f4 = "value1"
        sub.f4 = "value2"
    }
}
package de.honoka.test.various.test.movable

import java.io.Serializable
import java.lang.reflect.ParameterizedType

object ParameterTypesTest {

    data class TestEntity(val a: String, val b: Int) : Serializable

    class TestClass {

        fun test1(list: List<TestEntity>, map: Map<String, Int>, param1: String) {}
    }

    @JvmStatic
    fun main(args: Array<String>) {
        //println(Serializable::class.java.isAssignableFrom(TestEntity::class.java))
        TestClass::class.java.declaredMethods.forEach {
            it.genericParameterTypes.forEach { type ->
                println(type)
                if(type is ParameterizedType) {
                    println(type.rawType)
                    type.actualTypeArguments.forEach { typeArg ->
                        println(typeArg)
                    }
                }
                println()
            }
        }
    }
}
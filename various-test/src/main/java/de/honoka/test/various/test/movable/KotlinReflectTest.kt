package de.honoka.test.various.test.movable

import de.honoka.sdk.util.kotlin.basic.PropertyValueContainer
import org.junit.Test
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.jvm.javaMethod

@Suppress("MemberVisibilityCanBePrivate")
class KotlinReflectTest {
    
    interface Interface1 {
        
        var a: List<Int>
        
        fun b(): CallableReference = ::a as CallableReference
        
        fun c() = ::a.call()
    }
    
    interface Interface2 {
        
        var a: List<Int>?
            get() = PropertyValueContainer.getOrNull(::a)
            set(value) = PropertyValueContainer.set(::a, value)
        
        val b: List<Int>
            get() = PropertyValueContainer.getOrInit(::b, listOf(1, 2, 3))
    }
    
    class Class1(override var a: List<Int> = listOf()) : Interface1
    
    class Class2 : Interface2
    
    var class2Obj1: Class2? = Class2()
    
    var class2Obj2: Class2? = Class2()
    
    @Test
    fun test7() {
        var obj: Class2? = Class2()
        val ref = WeakReference(obj)
        val property = obj!!::a.getter.javaMethod
        println(ref.get())
        obj = null
        System.gc()
        TimeUnit.SECONDS.sleep(1)
        println(ref.get())
    }
    
    @Test
    fun test6() {
        class2Obj1!!.run {
            println(a)
            a = listOf(1, 2, 3, 4, 5)
            println(a)
            println(b)
        }
        class2Obj2!!.run {
            a = listOf(3, 2, 1)
            println(a)
        }
        class2Obj1 = null
        System.gc()
        TimeUnit.SECONDS.sleep(1)
        class2Obj2?.a
        TimeUnit.SECONDS.sleep(1)
        println()
    }
    
    @Test
    fun test5() {
        var class1: Class1? = Class1(listOf(1, 2, 3))
        val ref = WeakReference(class1)
        val set = setOf(class1!!.b())
        println(ref.get())
        class1 = null
        System.gc()
        TimeUnit.SECONDS.sleep(1)
        println(ref.get())
    }
    
    @Test
    fun test4() {
        val obj1 = Class1(listOf(1, 2, 3))
        val obj2 = Class1(listOf(4, 5, 6))
        println(obj1.c())
        println(obj2.c())
    }
    
    @Test
    fun test3() {
        val obj1 = Class1(listOf(1, 2, 3))
        val obj2 = Class1(listOf(4, 5, 6))
        val ref1 = WeakReference(obj1.b())
        val ref2 = WeakReference(obj2.b())
        println("${ref1.hashCode()} ${ref2.hashCode()}")
        println(ref1 == ref2)
        println("${obj1.hashCode()} ${obj2.hashCode()}")
        println("${ref1.get()?.hashCode()} ${ref2.get()?.hashCode()}")
        println(ref1.get() == ref2.get())
        println(obj1.b() == obj1.b())
        println(obj1.b() == obj2.b())
        println("${ref1.get()?.call()} ${ref2.get()?.call()}")
    }
    
    @Test
    fun test2() {
        var class1: Class1? = Class1()
        val ref = WeakReference(class1!!.b())
        //ref.get()!!.setter.call(listOf(1, 2, 3))
        println(ref.get()!!.call())
        class1 = null
        System.gc()
        TimeUnit.SECONDS.sleep(1)
        println(ref.get())
    }
    
    @Test
    fun test1() {
        val class1 = Class1()
        println(class1.b().call())
        println(class1::a.get())
        //class1.b().setter.call(listOf(1, 2, 3))
        println(class1.b().call())
    }
}

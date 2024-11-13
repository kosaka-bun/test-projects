package de.honoka.test.various.old.p4

import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy
import java.util.concurrent.TimeUnit

object ThreadPoolTest {
    
    private val executor = ThreadPoolExecutor(
        3, 5, 10, TimeUnit.SECONDS,
        SynchronousQueue(), AbortPolicy()
    )
    
    @JvmStatic
    fun main(args: Array<String>) {
        repeat(10) {
            try {
                executor.submit {
                    println(it)
                    TimeUnit.SECONDS.sleep(60)
                }
            } catch(t: Throwable) {
                System.err.println("failed: $it")
            }
            TimeUnit.SECONDS.sleep(2)
        }
        TimeUnit.SECONDS.sleep(60)
    }
}

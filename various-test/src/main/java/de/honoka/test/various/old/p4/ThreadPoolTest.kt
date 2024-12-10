package de.honoka.test.various.old.p4

import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadPoolTest {
    
    private val executor = ScheduledThreadPoolExecutor(1)
    
    @JvmStatic
    fun main(args: Array<String>) {
        executor.scheduleWithFixedDelay({
            TimeUnit.SECONDS.sleep(3)
            throw Exception().apply { printStackTrace() }
        }, 0, 5, TimeUnit.SECONDS)
        TimeUnit.SECONDS.sleep(60)
    }
}

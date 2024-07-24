package de.honoka.test.spring.item.executeinbatches

import cn.hutool.core.util.RandomUtil
import cn.hutool.core.util.ReflectUtil
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Suppress("UNCHECKED_CAST")
@RequestMapping("/executeInBatches")
@RestController
class AllController(private val batchScheduler: BatchScheduler) {

    @GetMapping("/test1")
    fun test1() = batchScheduler.init()

    @GetMapping("/test2")
    fun test2() {
        repeat(3) {
            Thread {
                println("任务1-${it + 1}开始")
                val seconds = RandomUtil.randomLong(10, 21)
                TimeUnit.SECONDS.sleep(seconds)
                batchScheduler.reportFinishedTask(1)
                println("任务1-${it + 1}完成，耗时：${seconds}秒")
            }.start()
        }
        repeat(5) {
            Thread {
                batchScheduler.waitForBatchNum(2, 60)
                println("任务2-${it + 1}开始")
                val seconds = RandomUtil.randomLong(10, 21)
                TimeUnit.SECONDS.sleep(seconds)
                batchScheduler.reportFinishedTask(2)
                println("任务2-${it + 1}完成，耗时：${seconds}秒")
            }.start()
        }
        repeat(2) {
            Thread {
                batchScheduler.waitForBatchNum(3, 60)
                println("任务3-${it + 1}开始")
                val seconds = RandomUtil.randomLong(10, 21)
                TimeUnit.SECONDS.sleep(seconds)
                batchScheduler.reportFinishedTask(3)
                println("任务3-${it + 1}完成，耗时：${seconds}秒")
            }.start()
        }
    }

    @GetMapping("/test3")
    fun test3(): Any = ReflectUtil.getFieldValue(batchScheduler, "batchNum")

    @GetMapping("/test4")
    fun test4(): Any = run {
        (ReflectUtil.getFieldValue(batchScheduler, "waitingThreads") as Set<Thread>).size
    }

    @GetMapping("/test5")
    fun test5() {
        val list = ArrayList<CompletableFuture<*>>()
        repeat(3) {
            val future = CompletableFuture.runAsync {
                TimeUnit.SECONDS.sleep(3L + it)
                if(it == 2) throw Exception()
            }
            list.add(future)
        }
        TimeUnit.SECONDS.sleep(10)
        list.forEach {
            println(it.get())
        }
    }
}
package de.honoka.test.kotlin.coroutine

import de.honoka.util.code.ColorfulText
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.system.exitProcess

object CoroutineTest {

	@JvmStatic
	fun main(args: Array<String>): Unit = runBlocking {
		val singleThreadContext = Executors.newSingleThreadExecutor()
				.asCoroutineDispatcher()
		val job1 = GlobalScope.launch(singleThreadContext) {
			fun1()
		}
		val job2 = GlobalScope.launch(singleThreadContext) {
			fun2()
		}
		GlobalScope.launch(singleThreadContext) {
			fun3()
		}
		println("主线程完成，等待协程")
		job1.join()
		printWithThread("job1完成状态：${job1.isCompleted}")
		printWithThread("job2完成状态：${job2.isCompleted}")
		exitProcess(0)
	}

	private suspend fun fun1() {
		for(i in 1..30) {
			ColorfulText.of().red("[${Thread.currentThread().name}] [${
				System.currentTimeMillis()}] fun1: $i").println()
			/*
             * delay方法可以主动让出当前协程对线程的占用，使线程可以去执行其他待执行的协程
             * 这样使得线程可以不用在等待的过程中被闲置，以实现非阻塞
             * delay方法中传递的参数为要等待的【最小】时间，单位为毫秒
             * 指的是【至少】在多少毫秒后再次执行本协程
             */
			if(i % 10 == 0) delay(1000)
		}
	}

	private suspend fun fun2() {
		for(i in 1..30) {
			ColorfulText.of().green("[${Thread.currentThread().name}] [${
				System.currentTimeMillis()}] fun2: $i").println()
			if(i % 10 == 0) delay(1)
		}
	}

	private suspend fun fun3() {
		for(i in 1..30) {
			ColorfulText.of().blue("[${Thread.currentThread().name}] [${
				System.currentTimeMillis()}] fun3: $i").println()
			if(i % 10 == 0) delay(1)
		}
	}
}


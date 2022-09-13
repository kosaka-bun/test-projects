package de.honoka.test.kotlin.coroutine

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.system.exitProcess

object CoroutineTest2 {

    @JvmStatic
    fun main(args: Array<String>) {
        val singleThreadContext = Executors.newSingleThreadExecutor()
                .asCoroutineDispatcher()
        //【主线程】在指定的协程容器中创建一个协程，然后继续往下执行代码
        GlobalScope.launch(singleThreadContext) {
            //1【子线程】
            printWithThread("print1")
            //2【子线程】协程让出线程占用，至少等待500ms（流程转移到"3"处）
            delay(500)
            //6【子线程】协程再次获得线程使用权，执行代码（执行完成后流程转移到"7"处）
            printWithThread("print2")
        }
        //【主线程】在指定的协程容器中创建一个协程，然后继续往下执行代码
        GlobalScope.launch(singleThreadContext) {
            //3【子线程】协程得到线程使用权，执行代码（执行完成后流程转移到"4"处）
            printWithThread("print3")
        }
        //【主线程】在指定的协程容器中创建一个协程，并等待创建的协程执行完毕后，继续往下执行
        runBlocking(singleThreadContext) {
            //4【子线程】协程得到线程使用权，执行代码
            printWithThread("print4")
            //5【子线程】协程让出线程占用，至少等待1000ms（流程转移到"6"处）
            delay(1000)
            //7【子线程】协程再次获得线程使用权，执行代码（执行完成后流程转移到"8"处）
            printWithThread("print5")
        }
        //【主线程】阻塞
        //8【主线程】上述代码块中的print5结束后，执行下面的代码
        printWithThread("print6")
        exitProcess(0)
    }
}
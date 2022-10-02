package de.honoka.test.kotlin.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object CoroutineTest3 {

    @JvmStatic
    fun main(args: Array<String>) {
        //在当前线程（主线程）上创建一个协程1
        runBlocking {
            //1.在主线程上创建一个新协程2
            launch {
                //5.协程2开始执行
                printWithThread("print1")
                //6.协程2挂起，切换到协程3
                delay(1)
                //12.协程2继续执行
                printWithThread("print2")
                //13.协程2执行完毕，切换到协程4
            }
            //2.在主线程上创建一个新协程3
            launch {
                //7.协程3开始执行
                printWithThread("print3")
                //8.协程3执行完毕，切换到协程4
            }
            //3.在主线程上创建一个新协程4
            runBlocking {
                //9.协程4开始执行
                printWithThread("print4")
                //10.协程4挂起，切换到协程1
                //14.协程4继续执行，但未达到足够长的等待时间，故直接挂起，切换到协程1
                //16.只要未达到等待时间，就直接挂起，然后切换到协程1
                delay(1000)
                //18.协程4继续执行
                printWithThread("print5")
                //19.协程4执行完成，切换到协程1
            }
            /*
             * 4.协程4创建完成，协程1由于runBlocking需等待协程4执行完成后才能继续执行，
             * 故协程1挂起，主线程开始执行其他等待中的协程
             */
            //11.由于协程4尚未执行完毕，协程1不执行任何内容，直接挂起，切换到协程2
            //15.协程4尚未执行完毕，协程1直接挂起，切换到协程4
            //17.只要协程4未执行完毕，就直接挂起，切换到协程4
            //20.协程1继续执行
            printWithThread("print6")
            //21.协程1执行完毕，主线程回到主流程中继续执行
        }
        //22.runBlocking等待协程1执行完毕后，继续执行主流程
        printWithThread("finished")
    }
}
/*
 * 执行结果：
 * [main] [1655223370785] print1
 * [main] [1655223370885] print3
 * [main] [1655223370886] print4
 * [main] [1655223370888] print2
 * [main] [1655223371887] print5
 * [main] [1655223371887] print6
 * [main] [1655223371888] finished
 *
 * 需要注意的是，如果将创建协程4所用的runBlocking改为launch，则执行结果为：
 * [main] [1655223446340] print6
 * [main] [1655223446422] print1
 * [main] [1655223446438] print3
 * [main] [1655223446439] print4
 * [main] [1655223446441] print2
 * [main] [1655223447442] print5
 * [main] [1655223447442] finished
 *
 * 通过print6可以看出，协程1虽然执行完成了所有的代码，但线程却未跳出用于启动
 * 协程1的runBlocking代码块。
 * 这说明，即使协程1执行完成了所有的代码，但在由其启动的协程2、协程3、协程4
 * 执行完成之前，协程1也仍会继续运行。
 *
 * runBlocking的作用：
 * 启动一个协程，并在协程代码执行完毕，且关联启动的所有其他协程执行完毕后，
 * 继续运行runBlocking后面的代码。
 */
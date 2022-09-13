package de.honoka.test.various.test.movable

import java.util.*

@Suppress("SameParameterValue")
object SortTest {

    class Timer {

        private var nanoTime: Long? = null

        fun start() {
            nanoTime = System.nanoTime()
        }

        fun end(): Double {
            val result = System.nanoTime() - nanoTime!!
            nanoTime = null
            return result / 1000.0
        }
    }

    private lateinit var randomArray: List<Int>

    private val randomArrayCopy: ArrayList<Int>
        get() = ArrayList(randomArray)

    @JvmStatic
    fun main(args: Array<String>) {
        flushRandomArray(20)
        val timer = Timer()
        println("randomArray: $randomArray")
        timer.start()
        println("冒泡：${bubbleSort()}，耗时：${timer.end()}μs")
        timer.start()
        println("快速：${fastSort()}，耗时：${timer.end()}μs")
        timer.start()
        println("归并：${mergeSort()}，耗时：${timer.end()}μs")
        timer.start()
        println("选择：${selectionSort()}，耗时：${timer.end()}μs")
        timer.start()
        println("插入：${insertionSort()}，耗时：${timer.end()}μs")
    }

    private fun flushRandomArray(count: Int) {
        val random = Random()
        randomArray = ArrayList<Int>().apply {
            for(i in 0 until count) {
                add(random.nextInt(100))
            }
        }
    }

    private fun bubbleSort(): List<Int> {
        val result = randomArrayCopy
        for(i in result.indices) {
            for(j in 1 until result.size - i) {
                if(result[j - 1] > result[j]) {
                    val temp = result[j - 1]
                    result[j - 1] = result[j]
                    result[j] = temp
                }
            }
        }
        return result
    }

    private fun fastSort(): List<Int> {
        val result = randomArrayCopy
        fun doFastSort(start: Int, end: Int) {
            var midIndex = start
            for(i in (start + 1)..end) {
                if(result[i] < result[start]) {
                    midIndex++
                    val temp = result[midIndex]
                    result[midIndex] = result[i]
                    result[i] = temp
                }
            }
            val temp = result[midIndex]
            result[midIndex] = result[start]
            result[start] = temp
            if(midIndex > start + 1) {
                doFastSort(start, midIndex - 1)
            }
            if(midIndex < end - 1) {
                doFastSort(midIndex + 1, end)
            }
        }
        doFastSort(0, result.size - 1)
        return result
    }

    private fun mergeSort(): List<Int> {
        val result = randomArrayCopy
        fun merge(left: Int, right: Int): List<Int> {
            if(left == right) return arrayListOf(result[left])
            val mid = (left + right) / 2
            val leftList = merge(left, mid)
            val rightList = merge(mid + 1, right)
            val newList = ArrayList<Int>()
            var i = 0; var j = 0
            while(i < leftList.size || j < rightList.size) {
                val leftValue = if(i < leftList.size) leftList[i] else null
                val rightValue = if(j < rightList.size) rightList[j] else null
                if(leftValue == null && rightValue != null) {
                    newList.add(rightValue)
                    j++
                    continue
                }
                if(leftValue != null && rightValue == null) {
                    newList.add(leftValue)
                    i++
                    continue
                }
                if(leftValue!! < rightValue!!) {
                    newList.add(leftValue)
                    i++
                } else {
                    newList.add(rightValue)
                    j++
                }
            }
            return newList
        }
        return merge(0, result.size - 1)
    }

    private fun selectionSort(): List<Int> {
        val result = randomArrayCopy
        for(i in 0 until result.size) {
            for(j in i + 1 until result.size ) {
                if(result[i] > result[j]) {
                    val temp = result[j]
                    result[j] = result[i]
                    result[i] = temp
                }
            }
        }
        return result
    }

    private fun insertionSort(): List<Int> {
        val result = randomArrayCopy
        for(i in 1 until result.size) {
            for(j in i downTo 1) {
                if(result[j - 1] < result[j]) break
                val temp = result[j - 1]
                result[j - 1] = result[j]
                result[j] = temp
            }
        }
        return result
    }
}
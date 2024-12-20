package de.honoka.test.various

import cn.hutool.core.date.DateTime
import cn.hutool.core.date.DateUnit
import org.junit.Test
import java.util.concurrent.TimeUnit

class KotlinAllTest {
    
    @Test
    fun test10() {
        val date1 = DateTime.now()
        TimeUnit.MILLISECONDS.sleep(3800)
        val date2 = DateTime.now()
        println(date2.between(date1, DateUnit.SECOND))
    }
}
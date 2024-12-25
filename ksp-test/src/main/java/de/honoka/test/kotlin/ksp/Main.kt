package de.honoka.test.kotlin.ksp

import de.honoka.test.kotlin.ksp.generated.log

@Logger
object Main {
    
    @JvmStatic
    fun main(args: Array<String>) {
        log.info("success")
    }
}

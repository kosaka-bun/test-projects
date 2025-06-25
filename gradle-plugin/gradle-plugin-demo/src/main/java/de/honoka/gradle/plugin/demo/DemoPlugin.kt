package de.honoka.gradle.plugin.demo

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectStateInternal
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DemoPlugin : Plugin<Project> {

    companion object {

        val log: Logger = LoggerFactory.getLogger(DemoPlugin::class.java)
    }

    override fun apply(project: Project) {
        log.info("${javaClass.simpleName} is applied.")
        val props = project.extensions.create("demoPlugin", DemoPluginProperties::class.java)
        project.tasks.register("hello") {
            doLast {
                println("(${props.project}) Hello from DemoPlugin. ${props.message}")
                props.otherMessage?.let {
                    println("The other message is: ${props.otherMessage}")
                }
                println("The message in ExtObj is: ${ExtObj.message}")
            }
        }
    }
}

abstract class DemoPluginProperties(internal val project: Project) {

    var message: String? = null

    var otherMessage: String? = null

    fun otherMessage(str: String) {
        otherMessage = str
    }
}

fun DemoPluginProperties.extFun() {}

/*
 * 全局对象的生成与Gradle守护进程相关，不管Gradle执行多少个任务，或执行多少次同一个任务，只要守护进程
 * 未被重新启动，该对象都只会初始化一次，然后在每次执行任务时复用。
 * 这意味着，如果在脚本中给该对象的属性赋了值，则即便在后续的操作中，把赋值语句去掉，也仍然能在后续的
 * 任务执行中，读取到之前赋给那个属性的值。
 * 因此，绝大多数情况下，都不建议使用全局对象存储应该仅供单次任务执行所使用的值。
 */
object ExtObj {

    var message: String? = null
}

fun Project.showAllProjectsState() {
    rootProject.allprojects.forEach {
        println("$it ${(it.state as ProjectStateInternal).isConfiguring}")
    }
    println(Thread.currentThread().name)
    println("$project isRoot: ${project == rootProject}")
    println("-----".repeat(5))
}

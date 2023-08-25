package de.honoka.test.various.test.movable

import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.system.exitProcess

object GuiTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val frame = JFrame().apply {
            title = "My Window"
            size = Dimension(1024, 768)
            defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE
            setLocationRelativeTo(null)
            addWindowListener(object : WindowAdapter() {

                override fun windowClosing(e: WindowEvent) {
                    println("closing")
                    JOptionPane.showConfirmDialog(
                        this@apply, "是否退出"
                    ).let {
                        when(it) {
                            JOptionPane.OK_OPTION -> exitProcess(0)
                            else -> {}
                        }
                    }
                }
            })
        }
        frame.isVisible = true
    }
}
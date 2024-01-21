package de.honoka.android.variousandroidtest.ui

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import cn.hutool.core.io.FileUtil
import de.honoka.android.variousandroidtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64

class InstalledAppReadTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_installed_app_read_test)
        CoroutineScope(Dispatchers.IO).launch {
            doAction()
        }
    }

    private fun doAction() {
        val iconSavePath = "${application.dataDir}/test/${javaClass.simpleName}/appIcons"
        File(iconSavePath).run {
            if(exists()) FileUtil.del(this)
            mkdirs()
        }
        packageManager.getInstalledApplications(0).forEach {
            if(!it.packageName.startsWith("de.honoka")) return@forEach
            val appName = packageManager.getApplicationLabel(it)
            println("\n$appName: ${it.packageName}")
            val icon = packageManager.getApplicationIcon(it)
            val iconBytes = ByteArrayOutputStream().run {
                icon.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, this)
                toByteArray()
            }
            File("$iconSavePath/${it.packageName}.png").run {
                createNewFile()
                outputStream().use { out ->
                    out.write(iconBytes)
                }
            }
            File("$iconSavePath/${it.packageName}.txt").run {
                createNewFile()
                writer().use { out ->
                    out.append("data:image/png;base64,")
                    out.append(Base64.getEncoder().encodeToString(iconBytes))
                }
            }
        }
    }
}
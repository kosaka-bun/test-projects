package de.honoka.android.variousandroidtest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import de.honoka.android.variousandroidtest.ui.DatabaseTestActivity
import de.honoka.android.variousandroidtest.ui.InstalledAppReadTestActivity
import de.honoka.sdk.util.android.common.GlobalComponents

class MainActivity : AppCompatActivity() {

    private val idActivityMap = mapOf(
        R.id.installedAppReadTestActivityBtn to InstalledAppReadTestActivity::class.java,
        R.id.databaseTestActivityBtn to DatabaseTestActivity::class.java
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalComponents.application = application
        initButtons()
    }

    private fun initButtons() {
        idActivityMap.forEach {
            findViewById<View>(it.key).setOnClickListener { _ ->
                startActivity(Intent(this, it.value))
            }
        }
    }
}
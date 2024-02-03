package de.honoka.android.variousandroidtest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.honoka.android.variousandroidtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_test)
        CoroutineScope(Dispatchers.IO).launch {
            doAction()
        }
    }

    private fun doAction() {

    }
}
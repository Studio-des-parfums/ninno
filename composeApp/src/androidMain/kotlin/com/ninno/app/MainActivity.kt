package com.ninno.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidAppContext.instance = applicationContext
        setContent {
            App(versionName = BuildConfig.VERSION_NAME)
        }
    }
}

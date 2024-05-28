package com.example.your_space

import android.app.Application
import timber.log.Timber

class YourSpaceApp : Application() {
    private val TAG = YourSpaceApp::class.java.simpleName

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

    }
}

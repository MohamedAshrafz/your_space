package com.example.your_space.utils

import android.util.Log
import timber.log.Timber

class LogCatTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.VERBOSE -> tag?.let { Timber.tag(it).v(message) }
            Log.DEBUG -> tag?.let { Timber.tag(it).d(message) }
            Log.INFO -> tag?.let { Timber.tag(it).i(message) }
            Log.WARN -> tag?.let { Timber.tag(it).w(message) }
            Log.ERROR -> tag?.let { Timber.tag(it).e(message) }
        }
    }

}
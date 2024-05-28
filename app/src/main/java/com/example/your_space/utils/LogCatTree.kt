package com.example.your_space.utils

import android.util.Log
import timber.log.Timber


class LogCatTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val caller = getCallerInfo(Throwable().stackTrace)
        when (priority) {
            Log.VERBOSE -> Log.v(tag, "$message[SOURCE $caller]")
            Log.DEBUG -> Log.d(tag, "$message[SOURCE $caller]")
            Log.INFO -> Log.i(tag, "$message[SOURCE $caller]")
            Log.WARN -> Log.w(tag, "$message[SOURCE $caller]")
            Log.ERROR -> Log.e(tag, "$message[SOURCE $caller]")
            else -> {}
        }
    }

    companion object {
        private fun getCallerInfo(stacks: Array<StackTraceElement>?): String {
            if (stacks == null || stacks.size < 5) {
                return ""
            }
            val stack = stacks[4]
            return String.format(" (%s:%s)", stack.fileName, stack.lineNumber)
        }
    }
}

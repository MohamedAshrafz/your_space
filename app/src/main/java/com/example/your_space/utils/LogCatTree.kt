package com.example.your_space.utils

import android.annotation.SuppressLint
import android.util.Log
import com.example.your_space.BuildConfig
import timber.log.Timber


class LogCatTree : Timber.Tree() {

    companion object {
        private const val STACK_TRACE_LEVEL = 4
        private const val MAX_NUMBER_OF_CALLERS = 3
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val caller = getCallerInfo(Throwable().stackTrace)
        when (priority) {
            Log.VERBOSE -> Log.v(tag, "$message  [SOURCE $caller]")
            Log.DEBUG -> Log.d(tag, "$message  [SOURCE $caller]")
            Log.INFO -> Log.i(tag, "$message  [SOURCE $caller]")
            Log.WARN -> Log.w(tag, "$message  [SOURCE $caller]")
            Log.ERROR -> Log.e(tag, "$message  [SOURCE $caller]")
            else -> {}
        }
    }

    @SuppressLint("LogNotTimber")
    private fun getCallerInfo(stacks: Array<StackTraceElement>?): String {
        if (stacks == null) {
            return ""
        }
        val stringBuilder = StringBuilder()
        var i = STACK_TRACE_LEVEL

        while (i <= STACK_TRACE_LEVEL + MAX_NUMBER_OF_CALLERS && i < stacks.size) {
            val stack = stacks[i]

            if (stack.className.contains(BuildConfig.APPLICATION_ID) && !stack.className.contains(LogCatTree::class.simpleName!!))
                if (i == STACK_TRACE_LEVEL)
                    stringBuilder.append(String.format("(%s:%s)", if (stack.fileName != null) stack.fileName else "Unknown Source", stack.lineNumber))
                else
                    stringBuilder.append(String.format(" , Caller: (%s:%s)", if (stack.fileName != null) stack.fileName else "Unknown Source", stack.lineNumber))

            i++
        }
        return stringBuilder.toString()
    }
}

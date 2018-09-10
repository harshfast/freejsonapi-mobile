package com.tak.freeapi.common

import android.util.Log
import com.tak.freeapi.BuildConfig


fun debugLog(tag: String = LogKt.TAG_DEBUG, message: Any?, messageStyle: String = LogKt.DEFAULT_EMPTY_PATTERN) {
    LogKt.printLog(Log.DEBUG, tag, message, messageStyle)
}

fun infoLog(tag: String = LogKt.TAG_INFO, message: Any?) {
    LogKt.printLog(Log.INFO, tag, message)
}

fun errorLog(tag: String = LogKt.TAG_ERROR, message: Any?) {
    LogKt.printLog(Log.ERROR, tag, message)
}

fun logException(exception: Exception?) {
    LogKt.printException(exception)
}

fun logExceptionOnlyMessage(exception: Exception?) {
    LogKt.printExceptionOnlyMessage(exception)
}

class LogKt {

    companion object {
        const val DEFAULT_EMPTY_PATTERN = ""
        const val TAG_DEBUG = "DEBUG"
        const val TAG_ERROR = "ERROR"
        const val TAG_INFO = "INFO"
        const val TAG_EXCEPTION = "An Exception Occurred: "

        fun printException(exception: Exception?, tag: String = LogKt.TAG_EXCEPTION) {
            printLog(Log.ERROR, tag, Log.getStackTraceString(exception))
        }

        fun printExceptionOnlyMessage(exception: Exception?, tag: String = LogKt.TAG_EXCEPTION) {
            printLog(Log.WARN, tag, exception?.message ?: "No exception message found")
        }

        fun printLog(logType: Int, tag: String = LogKt.TAG_DEBUG, message: Any?, messageStyle: String = DEFAULT_EMPTY_PATTERN) {

            if (BuildConfig.BUILD_TYPE.equals("release") && !Log.isLoggable(message.toString(), logType)) {
                return
            }

            when (logType) {
                Log.DEBUG -> Log.d(tag, "$messageStyle${message?.toString()}$messageStyle")
                Log.INFO -> Log.i(tag, message?.toString() ?: "null")
                Log.ERROR -> Log.e(tag, message?.toString() ?: "null")
                Log.WARN -> Log.w(tag, message?.toString() ?: "null")
            }

        }

    }


}
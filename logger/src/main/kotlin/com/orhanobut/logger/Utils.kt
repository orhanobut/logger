package com.orhanobut.logger

import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

internal object Utils {
  fun getStackTraceString(throwable: Throwable?): String {
    if (throwable == null) return ""
    var cause: Throwable? = throwable
    while (cause != null) {
      if (cause is UnknownHostException) return ""
      cause = cause.cause
    }
    val output = StringWriter()
    val writer = PrintWriter(output)
    throwable.printStackTrace(writer)
    writer.flush()
    return output.toString()
  }

  fun logLevel(priority: Int): String = when (priority) {
    Logger.VERBOSE -> "VERBOSE"
    Logger.DEBUG -> "DEBUG"
    Logger.INFO -> "INFO"
    Logger.WARN -> "WARN"
    Logger.ERROR -> "ERROR"
    Logger.ASSERT -> "ASSERT"
    else -> "UNKNOWN"
  }

  fun toString(obj: Any?): String = when (obj) {
    null -> "null"
    is BooleanArray -> obj.contentToString()
    is ByteArray -> obj.contentToString()
    is CharArray -> obj.contentToString()
    is ShortArray -> obj.contentToString()
    is IntArray -> obj.contentToString()
    is LongArray -> obj.contentToString()
    is FloatArray -> obj.contentToString()
    is DoubleArray -> obj.contentToString()
    is Array<*> -> obj.contentDeepToString()
    else -> obj.toString()
  }
}

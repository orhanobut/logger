package com.orhanobut.logger

/** Final destination of a formatted log message. */
fun interface LogStrategy {
  fun log(priority: Int, tag: String?, message: String)
}

package com.orhanobut.logger

/** Filters and routes messages through the logging pipeline. */
interface LogAdapter {
  fun isLoggable(priority: Int, tag: String?): Boolean
  fun log(priority: Int, tag: String?, message: String)
}

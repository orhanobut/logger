package com.orhanobut.logger

/** Formats messages before sending them to a [LogStrategy]. */
fun interface FormatStrategy {
  fun log(priority: Int, tag: String?, message: String)
}

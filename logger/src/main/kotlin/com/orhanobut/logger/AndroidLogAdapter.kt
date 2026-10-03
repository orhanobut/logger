package com.orhanobut.logger

/** Routes messages through pretty Logcat formatting by default. */
open class AndroidLogAdapter @JvmOverloads constructor(
  private val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder().build(),
) : LogAdapter {
  override fun isLoggable(priority: Int, tag: String?): Boolean = true
  override fun log(priority: Int, tag: String?, message: String) =
      formatStrategy.log(priority, tag, message)
}

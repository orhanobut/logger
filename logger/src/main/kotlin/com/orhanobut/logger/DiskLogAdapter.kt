package com.orhanobut.logger

/** Routes messages through CSV file formatting by default. */
open class DiskLogAdapter @JvmOverloads constructor(
  private val formatStrategy: FormatStrategy = CsvFormatStrategy.newBuilder().build(),
) : LogAdapter {
  override fun isLoggable(priority: Int, tag: String?): Boolean = true
  override fun log(priority: Int, tag: String?, message: String) =
      formatStrategy.log(priority, tag, message)
}

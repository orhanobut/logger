package com.orhanobut.logger

/** Captures output without Android calls or mocking frameworks. */
internal class RecordingLog : LogAdapter, LogStrategy, FormatStrategy {
  val entries = mutableListOf<Entry>()
  var minimumPriority = Logger.VERBOSE

  override fun isLoggable(priority: Int, tag: String?) = priority >= minimumPriority
  override fun log(priority: Int, tag: String?, message: String) {
    entries.add(Entry(priority, tag, message))
  }

  data class Entry(val priority: Int, val tag: String?, val message: String)
}

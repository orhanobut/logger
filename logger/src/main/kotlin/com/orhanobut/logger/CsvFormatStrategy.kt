package com.orhanobut.logger

import android.os.Environment
import android.os.HandlerThread
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Formats file logs as timestamp, date, level, tag, and message. */
open class CsvFormatStrategy private constructor(
  private val date: Date,
  private val dateFormat: SimpleDateFormat,
  private val logStrategy: LogStrategy,
  private val tag: String?,
) : FormatStrategy {
  override fun log(priority: Int, tag: String?, message: String) {
    val outputTag = if (!tag.isNullOrEmpty() && this.tag != tag) "${this.tag}-$tag" else this.tag
    date.time = System.currentTimeMillis()
    val content = message.replace(NEW_LINE, " <br> ")
    logStrategy.log(priority, outputTag,
        "${date.time},${dateFormat.format(date)},${Utils.logLevel(priority)},$outputTag,$content$NEW_LINE")
  }

  class Builder internal constructor() {
    private var date: Date? = null
    private var dateFormat: SimpleDateFormat? = null
    private var logStrategy: LogStrategy? = null
    private var tag: String? = "PRETTY_LOGGER"

    fun date(value: Date?): Builder = apply { date = value }
    fun dateFormat(value: SimpleDateFormat?): Builder = apply { dateFormat = value }
    fun logStrategy(value: LogStrategy?): Builder = apply { logStrategy = value }
    fun tag(tag: String?): Builder = apply { this.tag = tag }

    fun build(): CsvFormatStrategy {
      val date = date ?: Date().also { this.date = it }
      val format = dateFormat ?: SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK)
          .also { dateFormat = it }
      val strategy = logStrategy ?: defaultLogStrategy().also { logStrategy = it }
      return CsvFormatStrategy(date, format, strategy, tag)
    }

    @Suppress("DEPRECATION")
    private fun defaultLogStrategy(): LogStrategy {
      val folder = File(Environment.getExternalStorageDirectory(), "logger").absolutePath
      val thread = HandlerThread("AndroidFileLogger.$folder")
      thread.start()
      return DiskLogStrategy(DiskLogStrategy.WriteHandler(thread.looper, folder, MAX_BYTES))
    }
  }

  companion object {
    private const val MAX_BYTES = 500 * 1024
    private val NEW_LINE = System.getProperty("line.separator") ?: "\n"

    @JvmStatic fun newBuilder(): Builder = Builder()
  }
}

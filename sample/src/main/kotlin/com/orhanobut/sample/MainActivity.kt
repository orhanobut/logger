package com.orhanobut.sample

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.DiskLogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.io.File
import java.io.IOException

class MainActivity : Activity() {
  private var logThread: HandlerThread? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    Logger.clearLogAdapters()
    val format = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .tag("Sample")
        .build()
    Logger.addLogAdapter(object : AndroidLogAdapter(format) {
      override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
    })

    // App-private storage works with scoped storage without extra permissions.
    val thread = HandlerThread("SampleFileLogger").also { it.start() }
    logThread = thread
    val logFile = File(filesDir, "logger.csv")
    val handler = object : Handler(thread.looper) {
      override fun handleMessage(msg: Message) {
        try {
          logFile.appendText(msg.obj as String)
        } catch (_: IOException) {
          // A logging failure should not interrupt the sample.
        }
      }
    }
    val csv = CsvFormatStrategy.newBuilder().logStrategy(DiskLogStrategy(handler)).tag("Sample").build()
    Logger.addLogAdapter(object : DiskLogAdapter(csv) {
      override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
    })

    Logger.d("hello %s", "Kotlin")
    Logger.i("Messages also go to %s", logFile.absolutePath)
    Logger.t("once").w("This tag applies to one message")
    Logger.json("""{"key":3,"items":["foo","bar"]}""")
    Logger.d(listOf("foo", "bar"))
    Logger.d(mapOf("key" to "value", "key1" to "value2"))
  }

  override fun onDestroy() {
    Logger.clearLogAdapters()
    logThread?.quitSafely()
    super.onDestroy()
  }
}

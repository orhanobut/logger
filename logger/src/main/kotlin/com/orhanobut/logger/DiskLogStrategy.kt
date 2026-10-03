package com.orhanobut.logger

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.io.File
import java.io.FileWriter
import java.io.IOException

/** Sends file writes to an Android background handler. */
open class DiskLogStrategy(private val handler: Handler) : LogStrategy {
  override fun log(priority: Int, tag: String?, message: String) {
    handler.sendMessage(handler.obtainMessage(priority, message))
  }

  internal class WriteHandler(
    looper: Looper,
    private val folder: String,
    private val maxFileSize: Int,
  ) : Handler(looper) {
    override fun handleMessage(msg: Message) {
      val content = msg.obj as String
      val file = getLogFile()
      try {
        FileWriter(file, true).use { it.append(content) }
      } catch (_: IOException) {
        // Logging must not crash the application when storage is unavailable.
      }
    }

    private fun getLogFile(): File {
      val directory = File(folder)
      if (!directory.exists()) directory.mkdirs()
      var count = 0
      var next = File(directory, "logs_$count.csv")
      var existing: File? = null
      while (next.exists()) {
        existing = next
        next = File(directory, "logs_${++count}.csv")
      }
      return if (existing != null && existing.length() < maxFileSize) existing else next
    }
  }
}

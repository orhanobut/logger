package com.orhanobut.logger

import android.util.Log

/** Writes formatted messages to Android Logcat. */
open class LogcatLogStrategy : LogStrategy {
  override fun log(priority: Int, tag: String?, message: String) {
    Log.println(priority, tag ?: DEFAULT_TAG, message)
  }

  internal companion object {
    const val DEFAULT_TAG = "NO_TAG"
  }
}

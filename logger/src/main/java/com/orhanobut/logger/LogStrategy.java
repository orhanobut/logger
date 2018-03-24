package com.orhanobut.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Determines destination target for the logs such as Disk, Logcat etc.
 *
 * @see LogcatLogStrategy
 * @see DiskLogStrategy
 */
public interface LogStrategy {

  /**
   * This is invoked by Logger each time a log message is processed.
   * Interpret this method as last destination of the log in whole pipeline.
   *
   * @param priority is the log level e.g. DEBUG, WARNING
   * @param tag is the given tag for the log message.
   * @param message is the given message for the log message.
   */
  void log(int priority, @Nullable String tag, @NonNull String message);
}

package com.orhanobut.logger;

import android.os.Environment;

import java.io.File;

public final class Settings {

  private int methodCount = 2;
  private boolean showThreadInfo = true;
  private int methodOffset = 0;
  private LogAdapter logAdapter;
  private FileLogger fileLogger;

  /**
   * Determines to how logs will be printed
   */
  private int logLevel = LogLevel.VERBOSE;

  /**
   * Determines to how logs will be saved to file
   */
  private int fileLogLevel = LogLevel.DISABLED;

  public Settings hideThreadInfo() {
    showThreadInfo = false;
    return this;
  }

  public Settings methodCount(int methodCount) {
    if (methodCount < 0) {
      methodCount = 0;
    }
    this.methodCount = methodCount;
    return this;
  }

  public Settings logLevel(LogLevel logLevel) {
    this.logLevel = logLevel.getValue();
    return this;
  }

  public Settings logLevel(int logLevel) {
    this.logLevel = logLevel;
    return this;
  }

  public Settings methodOffset(int offset) {
    this.methodOffset = offset;
    return this;
  }

  public Settings logAdapter(LogAdapter logAdapter) {
    this.logAdapter = logAdapter;
    return this;
  }

  public Settings fileLogLevel(int fileLogLevel) {
    this.fileLogLevel = fileLogLevel;
    return this;
  }

  public Settings fileLogger(FileLogger fileLogger) {
    this.fileLogger = fileLogger;
    return this;
  }

  int getFileLogLevel() {
    return fileLogLevel;
  }

  int getMethodCount() {
    return methodCount;
  }

  boolean isShowThreadInfo() {
    return showThreadInfo;
  }

  int getLogLevel() {
    return logLevel;
  }

  int getMethodOffset() {
    return methodOffset;
  }

  LogAdapter getLogAdapter() {
    if (logAdapter == null) {
      logAdapter = new AndroidLogAdapter();
    }
    return logAdapter;
  }

  /**
   * Returns the instance of FileLogger, creating one if necessary.
   * The created logger is a CSV logger and points to the folder "logger" on external storage
   * If developer wants to use internal folder, he/she must call {@link #fileLogger(FileLogger)}
   *
   * @return file logger
   */
  FileLogger getFileLogger() {
    if (fileLogger == null) {
      fileLogger = new AndroidCsvFileLogger(
          Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "logger");
    }
    return fileLogger;
  }

  void reset() {
    methodCount = 2;
    methodOffset = 0;
    showThreadInfo = true;
    logLevel = LogLevel.VERBOSE;
  }
}
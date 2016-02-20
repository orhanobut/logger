package com.orhanobut.logger;

public final class Settings {

  private int methodCount = 2;
  private boolean showThreadInfo = true;
  private int methodOffset = 0;
  private LogTool logTool;
  private boolean logValve = true;//a valve to set logger work or not

  /**
   * Determines how logs will printed
   */
  private LogLevel logLevel = LogLevel.FULL;

  public Settings hideThreadInfo() {
    showThreadInfo = false;
    return this;
  }

  /***
   * set a valve to control logger work or not-work
   * use with BuildConfig.DEBUG is a good choice
   * @param valve if true, logger work,or logger not work
   * @return Settings
   */
  public Settings setLogValve(boolean valve){
    this.logValve = valve;
    return this;
  }

  /**
   * Use {@link #methodCount}
   */
  @Deprecated public Settings setMethodCount(int methodCount) {
    return methodCount(methodCount);
  }

  public Settings methodCount(int methodCount) {
    if (methodCount < 0) {
      methodCount = 0;
    }
    this.methodCount = methodCount;
    return this;
  }

  /**
   * Use {@link #logLevel}
   */
  @Deprecated
  public Settings setLogLevel(LogLevel logLevel) {
    return logLevel(logLevel);
  }

  public Settings logLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
    return this;
  }

  /**
   * Use {@link #methodOffset}
   */
  @Deprecated public Settings setMethodOffset(int offset) {
    return methodOffset(offset);
  }

  public Settings methodOffset(int offset) {
    this.methodOffset = offset;
    return this;
  }

  public Settings logTool(LogTool logTool) {
    this.logTool = logTool;
    return this;
  }

  public int getMethodCount() {
    return methodCount;
  }

  public boolean isShowThreadInfo() {
    return showThreadInfo;
  }

  public LogLevel getLogLevel() {
    return logLevel;
  }

  public int getMethodOffset() {
    return methodOffset;
  }

  public LogTool getLogTool() {
    if (logTool == null) {
      logTool = new AndroidLogTool();
    }
    return logTool;
  }

  public boolean getLogValve() {
    return logValve;
  }
}

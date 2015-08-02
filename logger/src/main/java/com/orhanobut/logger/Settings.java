package com.orhanobut.logger;

/**
 * @author Orhan Obut
 */
public final class Settings {

  private int methodCount = 2;
  private boolean showThreadInfo = true;
  private int methodOffset = 0;

  /**
   * Determines how logs will printed
   */
  private LogLevel logLevel = LogLevel.FULL;

  public Settings hideThreadInfo() {
    showThreadInfo = false;
    return this;
  }

  public Settings setMethodCount(int methodCount) {
    if (methodCount < 0) {
      methodCount = 0;
    }
    this.methodCount = methodCount;
    return this;
  }

  public Settings setLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
    return this;
  }

  public Settings setMethodOffset(int offset) {
    this.methodOffset = offset;
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
}

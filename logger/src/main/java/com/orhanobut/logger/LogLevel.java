package com.orhanobut.logger;

public enum LogLevel {

  /**
   * Prints all logs
   */
  FULL(2),

  /**
   * No log will be printed
   */
  NONE(0);


  private final int value;

  LogLevel(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static final int DISABLED = 0;
  public static final int VERBOSE = 2;
  public static final int DEBUG = 3;
  public static final int INFO = 4;
  public static final int WARN = 5;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;

  public static String toString(int value) {
    switch (value) {
      case DISABLED:
        return "DISABLED";
      case VERBOSE:
        return "VERBOSE";
      case DEBUG:
        return "DEBUG";
      case INFO:
        return "INFO";
      case WARN:
        return "WARN";
      case ERROR:
        return "ERROR";
      case ASSERT:
        return "ASSERT";
      default:
        return "UNKNOWN";
    }
  }
}
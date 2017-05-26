package com.orhanobut.logger;

public class LogItem {
  public final int priority;
  public final String tag;
  public final String message;

  public LogItem(int priority, String tag, String message) {
    this.priority = priority;
    this.tag = tag;
    this.message = message;
  }
}

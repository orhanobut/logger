package com.orhanobut.logger;

public class AndroidLogAdapter implements LogAdapter {

  private final FormatStrategy formatStrategy;

  public AndroidLogAdapter() {
    this.formatStrategy = PrettyFormatStrategy.newBuilder().build();
  }

  public AndroidLogAdapter(FormatStrategy formatStrategy) {
    this.formatStrategy = formatStrategy;
  }

  @Override public boolean isLoggable(int priority, String tag) {
    return true;
  }

  @Override public void log(int priority, String tag, String message) {
    formatStrategy.log(priority, tag, message);
  }

}
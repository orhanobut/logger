package com.orhanobut.logger;

public interface Printer {

  Printer t(String tag, int methodCount);

  Printer h(int actFragHashCode);

  Settings init(String tag);

  Settings getSettings();

  void d(String message, Object... args);

  void d(Object object);

  void e(String message, Object... args);

  void e(Throwable throwable, String message, Object... args);

  void w(String message, Object... args);

  void i(String message, Object... args);

  void v(String message, Object... args);

  void wtf(String message, Object... args);

  void json(String json);

  void xml(String xml);

  void lifecycle(String className, String methodName);

  void log(int priority, boolean isLifecycleLog, String tag, String message, Throwable throwable);

  void resetSettings();

}
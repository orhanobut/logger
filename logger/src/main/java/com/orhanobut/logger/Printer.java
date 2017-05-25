package com.orhanobut.logger;

interface Printer {

  Printer t(String tag, int methodCount);

  /**
   * Add custom settings with a custom tag
   *
   * @param tag is the given string which will be used in Logger
   */
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

  /** Formats the given json content and print it */
  void json(String json);

  /** Formats the given xml content and print it */
  void xml(String xml);

  void log(int priority, String tag, String message, Throwable throwable);

  void resetSettings();

}

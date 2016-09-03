package com.orhanobut.logger;

/**
 * Logger is a wrapper of {@link android.util.Log}
 * But more pretty, simple and powerful
 */
public final class Logger {
  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;
  public static final int INFO = 4;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;

  private static final String DEFAULT_TAG = "PRETTYLOGGER";

  private static Printer printer = new LoggerPrinter();

  //no instance
  private Logger() {
  }

  /**
   * It is used to get the settings object in order to change settings
   *
   * @return the settings object
   */
  public static Settings init() {
    return init(DEFAULT_TAG);
  }

  /**
   * It is used to change the tag
   *
   * @param tag is the given string which will be used in Logger as TAG
   */
  public static Settings init(String tag) {
    printer = new LoggerPrinter();
    return printer.init(tag);
  }

  public static void resetSettings() {
    printer.resetSettings();
  }

  public static Printer t(String tag) {
    return printer.t(tag, printer.getSettings().getMethodCount());
  }

  public static Printer t(int methodCount) {
    return printer.t(null, methodCount);
  }

  public static Printer t(String tag, int methodCount) {
    return printer.t(tag, methodCount);
  }

  /**
   * Intended for use by {@link com.orhanobut.logger.LoggerActivity} and
   * {@link com.orhanobut.logger.LoggerFragment} only, otherwise the hash code
   * cannot reliably be paired with the appropriate class in the log printout.
   */
  public static Printer h(int actFragHashCode) {
    return printer.h(actFragHashCode);
  }

  public static void log(int priority, String tag, String message, Throwable throwable) {
    printer.log(priority, false, tag, message, throwable);
  }

  public static void d(String message, Object... args) {
    printer.d(message, args);
  }

  public static void d(Object object) {
    printer.d(object);
  }

  public static void e(String message, Object... args) {
    printer.e(null, message, args);
  }

  public static void e(Throwable throwable, String message, Object... args) {
    printer.e(throwable, message, args);
  }

  public static void i(String message, Object... args) {
    printer.i(message, args);
  }

  public static void v(String message, Object... args) {
    printer.v(message, args);
  }

  public static void w(String message, Object... args) {
    printer.w(message, args);
  }

  public static void wtf(String message, Object... args) {
    printer.wtf(message, args);
  }

  /**
   * Formats the json content and print it
   *
   * @param json the json content
   */
  public static void json(String json) {
    printer.json(json);
  }

  /**
   * Formats the json content and print it
   *
   * @param xml the xml content
   */
  public static void xml(String xml) {
    printer.xml(xml);
  }

  /**
   * Intended for use by {@link com.orhanobut.logger.LoggerActivity} and
   * {@link com.orhanobut.logger.LoggerFragment} only, for logging lifecycle
   * callbacks.
   *
   * <p>Getting the class name and the method name from the thread stack trace
   * is unreliable for lifecycle callbacks. If a child class of
   * {@link com.orhanobut.logger.LoggerActivity} or
   * {@link com.orhanobut.logger.LoggerFragment} does not override the lifecycle
   * callback, the method name in the stacktrace may be different (i.e., callActivityOnStart vs OnStart),
   * and only the parent class name, not the child class name, will be present.</p>
   */
  public static void lifecycle(String className, String methodName){
    printer.lifecycle(className, methodName);
  }
}
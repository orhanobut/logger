package com.orhanobut.logger;

/**
 * Logger is a wrapper of {@link android.util.Log}
 * But more pretty, simple and powerful
 */
public final class Logger {

  private static final String DEFAULT_TAG = "PRETTY_LOGGER";

  private static Printer printer = new LoggerPrinter();

  private Logger() {
    //no instance
  }

  /** Provides an api to change setting */
  public static Settings init() {
    return init(DEFAULT_TAG);
  }

  /**  Provides an api to change settings with a custom tag */
  public static Settings init(String tag) {
    printer = new LoggerPrinter();
    return printer.init(tag);
  }

  /**
   * Reset all log settings to following default values:
   * <p>
   * Tag: PRETTY_LOGGER
   * Method count: 2
   * Show thread info: true
   * Method offset: 0
   * Log adapter: Android log adapter
   * File logger: None
   */
  public static void resetSettings() {
    printer.resetSettings();
  }

  /**
   * Given tag will be used as tag only once for this method call regardless of the tag that's been
   * set during initialization. After this invocation, the general tag that's been set will
   * be used for the subsequent log calls
   */
  public static Printer t(String tag) {
    return printer.t(tag, printer.getSettings().getMethodCount());
  }

  /**
   * Given method count will be used only once for this method call regardless of the method count
   * that's been set during initialization. After this invocation, the general method count that's been
   * set will be used for the subsequent log calls
   */
  public static Printer t(int methodCount) {
    return printer.t(null, methodCount);
  }

  /**
   * Given tag and method count will be used only once for this method call regardless of the tag and method count
   * that's been set during initialization. After this invocation, the general tag and method count that's been
   * set will be used for the subsequent log calls
   */
  public static Printer t(String tag, int methodCount) {
    return printer.t(tag, methodCount);
  }

  /** General log function that accepts all configurations as parameter */
  public static void log(int priority, String tag, String message, Throwable throwable) {
    printer.log(priority, tag, message, throwable);
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

  /**
   * Tip: Use this for exceptional situations to log
   * ie: Unexpected errors etc
   */
  public static void wtf(String message, Object... args) {
    printer.wtf(message, args);
  }

  /** Formats the given json content and print it */
  public static void json(String json) {
    printer.json(json);
  }

  /** Formats the given xml content and print it */
  public static void xml(String xml) {
    printer.xml(xml);
  }

}

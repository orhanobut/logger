package com.orhanobut.logger

/** Entry point for logging through registered [LogAdapter] instances. */
object Logger {
  const val VERBOSE = 2
  const val DEBUG = 3
  const val INFO = 4
  const val WARN = 5
  const val ERROR = 6
  const val ASSERT = 7

  private var printer: Printer = LoggerPrinter()

  @JvmStatic fun printer(printer: Printer) { this.printer = printer }
  @JvmStatic fun addLogAdapter(adapter: LogAdapter) = printer.addAdapter(adapter)
  @JvmStatic fun clearLogAdapters() = printer.clearLogAdapters()

  /** Uses [tag] for the next message on the calling thread only. */
  @JvmStatic fun t(tag: String?): Printer = printer.t(tag)

  @JvmStatic fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?) =
      printer.log(priority, tag, message, throwable)

  @JvmStatic fun d(message: String, vararg args: Any?) = printer.d(message, *args)
  @JvmStatic fun d(obj: Any?) = printer.d(obj)
  @JvmStatic fun e(message: String, vararg args: Any?) = printer.e(null, message, *args)
  @JvmStatic fun e(throwable: Throwable?, message: String, vararg args: Any?) =
      printer.e(throwable, message, *args)
  @JvmStatic fun i(message: String, vararg args: Any?) = printer.i(message, *args)
  @JvmStatic fun v(message: String, vararg args: Any?) = printer.v(message, *args)
  @JvmStatic fun w(message: String, vararg args: Any?) = printer.w(message, *args)
  @JvmStatic fun wtf(message: String, vararg args: Any?) = printer.wtf(message, *args)
  @JvmStatic fun json(json: String?) = printer.json(json)
  @JvmStatic fun xml(xml: String?) = printer.xml(xml)
}

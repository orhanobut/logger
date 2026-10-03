package com.orhanobut.logger

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

internal class LoggerPrinter : Printer {
  private val localTag = ThreadLocal<String>()
  private val logAdapters = mutableListOf<LogAdapter>()

  override fun t(tag: String?): Printer = apply {
    if (tag != null) localTag.set(tag)
  }

  override fun d(message: String, vararg args: Any?) = logMessage(Logger.DEBUG, null, message, args)
  override fun d(obj: Any?) = logMessage(Logger.DEBUG, null, Utils.toString(obj), emptyArray())
  override fun e(message: String, vararg args: Any?) = logMessage(Logger.ERROR, null, message, args)
  override fun e(throwable: Throwable?, message: String, vararg args: Any?) =
      logMessage(Logger.ERROR, throwable, message, args)
  override fun w(message: String, vararg args: Any?) = logMessage(Logger.WARN, null, message, args)
  override fun i(message: String, vararg args: Any?) = logMessage(Logger.INFO, null, message, args)
  override fun v(message: String, vararg args: Any?) = logMessage(Logger.VERBOSE, null, message, args)
  override fun wtf(message: String, vararg args: Any?) = logMessage(Logger.ASSERT, null, message, args)

  override fun json(json: String?) {
    if (json.isNullOrEmpty()) {
      d("Empty/Null json content")
      return
    }
    try {
      val content = json.trim()
      when {
        content.startsWith("{") -> d(JSONObject(content).toString(2))
        content.startsWith("[") -> d(JSONArray(content).toString(2))
        else -> e("Invalid Json")
      }
    } catch (_: JSONException) {
      e("Invalid Json")
    }
  }

  override fun xml(xml: String?) {
    if (xml.isNullOrEmpty()) {
      d("Empty/Null xml content")
      return
    }
    try {
      val output = StringWriter()
      val transformer = TransformerFactory.newInstance().newTransformer()
      transformer.setOutputProperty(OutputKeys.INDENT, "yes")
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
      transformer.transform(StreamSource(StringReader(xml)), StreamResult(output))
      d(output.toString().replaceFirst(">", ">\n"))
    } catch (_: TransformerException) {
      e("Invalid xml")
    }
  }

  @Synchronized
  override fun log(priority: Int, tag: String?, message: String?, throwable: Throwable?) {
    val content = when {
      throwable == null -> message
      message == null -> Utils.getStackTraceString(throwable)
      else -> "$message : ${Utils.getStackTraceString(throwable)}"
    }.takeUnless { it.isNullOrEmpty() } ?: "Empty/NULL log message"
    for (adapter in logAdapters) {
      if (adapter.isLoggable(priority, tag)) adapter.log(priority, tag, content)
    }
  }

  override fun clearLogAdapters() = logAdapters.clear()
  override fun addAdapter(adapter: LogAdapter) { logAdapters.add(adapter) }

  @Synchronized
  private fun logMessage(priority: Int, throwable: Throwable?, message: String, args: Array<out Any?>) {
    val tag = localTag.get()
    localTag.remove()
    val content = if (args.isEmpty()) message else String.format(message, *args)
    log(priority, tag, content, throwable)
  }
}

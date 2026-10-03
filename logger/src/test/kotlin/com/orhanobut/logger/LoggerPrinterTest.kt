package com.orhanobut.logger

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.atomic.AtomicReference

class LoggerPrinterTest {
  private val printer = LoggerPrinter()
  private val output = RecordingLog()

  @Before fun setUp() { printer.addAdapter(output) }

  @Test fun formatsArgumentsButPreservesLiteralPercentSigns() {
    printer.d("hello %s, %d", "world", 3)
    printer.d("100% complete")
    printer.d("literal %s")
    assertEquals(listOf("hello world, 3", "100% complete", "literal %s"),
        output.entries.map { it.message })
  }

  @Test fun filtersEachAdapterIndependentlyAndClearsThem() {
    val errorsOnly = RecordingLog().apply { minimumPriority = Logger.ERROR }
    printer.addAdapter(errorsOnly)
    printer.d("debug")
    printer.e("error")
    printer.clearLogAdapters()
    printer.e("removed")
    assertEquals(2, output.entries.size)
    assertEquals(listOf(RecordingLog.Entry(Logger.ERROR, null, "error")), errorsOnly.entries)
  }

  @Test fun consumesOneTimeTagForExactlyOneMessage() {
    printer.t("once").d("first")
    printer.d("second")
    assertEquals(listOf("once", null), output.entries.map { it.tag })
  }

  @Test fun keepsOneTimeTagsLocalToTheirThread() {
    printer.t("main")
    val failure = AtomicReference<Throwable>()
    val worker = Thread {
      try {
        printer.t("worker").d("worker message")
      } catch (error: Throwable) {
        failure.set(error)
      }
    }
    worker.start()
    worker.join()
    failure.get()?.let { throw AssertionError(it) }
    printer.d("main message")
    printer.d("untagged")
    assertEquals(listOf("worker", "main", null), output.entries.map { it.tag })
  }

  @Test fun includesThrowableAndCauseWithOptionalMessage() {
    val error = IllegalStateException("failed", Exception("cause"))
    printer.e(error, "operation %s", "save")
    printer.log(Logger.ERROR, "explicit", null, error)
    assertEquals(2, output.entries.size)
    assertTrue(output.entries[0].message.startsWith("operation save : java.lang.IllegalStateException: failed"))
    assertTrue(output.entries[0].message.contains("Caused by: java.lang.Exception: cause"))
    assertEquals("explicit", output.entries[1].tag)
    assertTrue(output.entries[1].message.startsWith("java.lang.IllegalStateException: failed"))
  }

  @Test fun suppliesFallbackForEmptyMessages() {
    printer.log(Logger.DEBUG, null, null, null)
    printer.d("")
    assertEquals(listOf("Empty/NULL log message", "Empty/NULL log message"),
        output.entries.map { it.message })
  }

  @Test fun formatsNestedArraysAndNullObjects() {
    printer.d(arrayOf<Any>(intArrayOf(1, 2), arrayOf("a", "b")))
    printer.d(null as Any?)
    assertEquals(listOf("[[1, 2], [a, b]]", "null"), output.entries.map { it.message })
  }

  @Test fun prettyPrintsJsonObjectsAndArrays() {
    printer.json("  {\"items\":[1,2]}  ")
    printer.json("[1,2]")
    assertEquals(listOf("{\"items\": [\n  1,\n  2\n]}", "[\n  1,\n  2\n]"),
        output.entries.map { it.message })
    assertTrue(output.entries.all { it.priority == Logger.DEBUG })
  }

  @Test fun reportsMalformedJsonWithoutThrowing() {
    listOf("not json", "{ missing end", "[1,").forEach(printer::json)
    assertEquals(3, output.entries.size)
    assertTrue(output.entries.all { it.priority == Logger.ERROR && it.message == "Invalid Json" })
  }

  @Test fun handlesEmptyJsonAndXml() {
    printer.json(null)
    printer.json("")
    printer.xml(null)
    printer.xml("")
    assertEquals(listOf("Empty/Null json content", "Empty/Null json content",
        "Empty/Null xml content", "Empty/Null xml content"), output.entries.map { it.message })
  }

  @Test fun formatsXmlAndReportsMalformedXml() {
    printer.xml("<root><child>value</child></root>")
    printer.xml("<root>")
    assertEquals(2, output.entries.size)
    assertEquals(Logger.DEBUG, output.entries[0].priority)
    assertTrue(output.entries[0].message.contains("\n"))
    assertTrue(output.entries[0].message.contains("<child>value</child>"))
    assertEquals(RecordingLog.Entry(Logger.ERROR, null, "Invalid xml"), output.entries[1])
  }

  @Test fun rejectsNullArgumentsFromJvmCallersBeforeWritingAnyOutput() {
    // Kotlin rejects these calls at compile time; reflection exercises the JVM boundary.
    val adapterError = assertThrows(InvocationTargetException::class.java) {
      LoggerPrinter::class.java.getMethod("addAdapter", LogAdapter::class.java).invoke(printer, null)
    }
    val messageError = assertThrows(InvocationTargetException::class.java) {
      LoggerPrinter::class.java.getMethod("d", String::class.java, Array<Any?>::class.java)
          .invoke(printer, null, emptyArray<Any?>())
    }
    val arrayError = assertThrows(InvocationTargetException::class.java) {
      LoggerPrinter::class.java.getMethod("d", String::class.java, Array<Any?>::class.java)
          .invoke(printer, "message", null)
    }
    assertTrue(adapterError.targetException is NullPointerException)
    assertTrue(messageError.targetException is NullPointerException)
    assertTrue(arrayError.targetException is NullPointerException)
    assertTrue(output.entries.isEmpty())
  }
}

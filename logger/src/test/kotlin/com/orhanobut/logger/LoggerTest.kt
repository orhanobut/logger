package com.orhanobut.logger

import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier

class LoggerTest {
  private val output = RecordingLog()

  @Before fun setUp() {
    Logger.printer(LoggerPrinter())
    Logger.addLogAdapter(output)
  }

  @After fun tearDown() { Logger.printer(LoggerPrinter()) }

  @Test fun routesAllLogLevelsThroughTheConfiguredPrinter() {
    Logger.v("verbose %s", "message")
    Logger.d("debug %s", "message")
    Logger.i("info %s", "message")
    Logger.w("warning %s", "message")
    Logger.e("error %s", "message")
    Logger.wtf("assert %s", "message")
    assertEquals(listOf(Logger.VERBOSE, Logger.DEBUG, Logger.INFO, Logger.WARN, Logger.ERROR, Logger.ASSERT),
        output.entries.map { it.priority })
    assertEquals(listOf("verbose message", "debug message", "info message", "warning message",
        "error message", "assert message"), output.entries.map { it.message })
    assertTrue(output.entries.all { it.tag == null })
  }

  @Test fun routesTagsObjectsStructuredContentAndExceptions() {
    Logger.t("once").d("tagged")
    Logger.d(intArrayOf(1, 2))
    Logger.json("[1]")
    Logger.xml("<root>value</root>")
    Logger.e(IllegalStateException("failed"), "save")
    Logger.log(Logger.WARN, "explicit", "direct", null)
    assertEquals(6, output.entries.size)
    assertEquals("once", output.entries[0].tag)
    assertNull(output.entries[1].tag)
    assertEquals("[1, 2]", output.entries[1].message)
    assertEquals("[1]", output.entries[2].message)
    assertTrue(output.entries[3].message.contains("<root>value</root>"))
    assertEquals(Logger.ERROR, output.entries[4].priority)
    assertTrue(output.entries[4].message.contains("save : java.lang.IllegalStateException: failed"))
    assertEquals(RecordingLog.Entry(Logger.WARN, "explicit", "direct"), output.entries[5])
  }

  @Test fun replacingPrinterAndClearingAdaptersRemovesOldOutputs() {
    Logger.printer(LoggerPrinter())
    Logger.d("old adapter removed")
    assertTrue(output.entries.isEmpty())
    Logger.addLogAdapter(output)
    Logger.d("new printer")
    Logger.clearLogAdapters()
    Logger.d("cleared")
    assertEquals(listOf(RecordingLog.Entry(Logger.DEBUG, null, "new printer")), output.entries)
  }

  @Test fun preservesStaticJvmEntryPointsAndRejectsNullConfiguration() {
    for ((method, type) in listOf("printer" to Printer::class.java, "addLogAdapter" to LogAdapter::class.java)) {
      val entryPoint = Logger::class.java.getMethod(method, type)
      assertTrue(Modifier.isStatic(entryPoint.modifiers))
      val error = assertThrows(InvocationTargetException::class.java) { entryPoint.invoke(null, null) }
      assertTrue(error.targetException is NullPointerException)
    }
    Logger.d("still configured")
    assertEquals(listOf(RecordingLog.Entry(Logger.DEBUG, null, "still configured")), output.entries)
  }
}

package com.orhanobut.logger

import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CsvFormatStrategyTest {
  private val output = RecordingLog()

  @Test fun writesTimestampLevelTagAndMessageOnOneLine() {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT).apply {
      timeZone = TimeZone.getTimeZone("UTC")
    }
    val strategy = CsvFormatStrategy.newBuilder().logStrategy(output).dateFormat(format).tag("app").build()
    strategy.log(Logger.WARN, "screen", "first" + System.lineSeparator() + "second")
    assertEquals(1, output.entries.size)
    val entry = output.entries.single()
    assertEquals(Logger.WARN, entry.priority)
    assertEquals("app-screen", entry.tag)
    assertTrue(entry.message.endsWith(System.lineSeparator()))
    val fields = entry.message.removeSuffix(System.lineSeparator()).split(',', limit = 5)
    assertEquals(5, fields.size)
    val timestamp = fields[0].toLong()
    assertTrue(timestamp > 0)
    assertEquals(format.format(Date(timestamp)), fields[1])
    assertEquals(listOf("WARN", "app-screen", "first <br> second"), fields.drop(2))
  }

  @Test fun usesDefaultTagAndAvoidsDuplicatingCustomTags() {
    CsvFormatStrategy.newBuilder().logStrategy(output).build().log(Logger.DEBUG, null, "default")
    val strategy = CsvFormatStrategy.newBuilder().logStrategy(output).tag("app").build()
    strategy.log(Logger.INFO, "app", "same")
    strategy.log(Logger.INFO, "", "empty")
    assertEquals(listOf("PRETTY_LOGGER", "app", "app"), output.entries.map { it.tag })
    assertTrue(output.entries[1].message.contains(",INFO,app,same"))
  }

  @Test fun rejectsNullMessagesFromJvmCallersBeforeWritingAnyOutput() {
    val error = assertThrows(InvocationTargetException::class.java) {
      CsvFormatStrategy::class.java.getMethod("log", Int::class.javaPrimitiveType, String::class.java,
          String::class.java).invoke(CsvFormatStrategy.newBuilder().logStrategy(output).build(),
          Logger.DEBUG, null, null)
    }
    assertTrue(error.targetException is NullPointerException)
    assertTrue(output.entries.isEmpty())
  }
}

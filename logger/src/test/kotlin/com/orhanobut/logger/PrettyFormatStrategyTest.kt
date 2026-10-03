package com.orhanobut.logger

import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.InvocationTargetException

class PrettyFormatStrategyTest {
  private val output = RecordingLog()

  private fun messageOnly() = PrettyFormatStrategy.newBuilder().logStrategy(output)
      .showThreadInfo(false).methodCount(0)

  @Test fun framesAndSplitsMultilineMessages() {
    messageOnly().build().log(Logger.INFO, null, "first" + System.lineSeparator() + "second")
    assertEquals(4, output.entries.size)
    assertTrue(output.entries.first().message.startsWith("┌"))
    assertEquals("│ first", output.entries[1].message)
    assertEquals("│ second", output.entries[2].message)
    assertTrue(output.entries.last().message.startsWith("└"))
    assertTrue(output.entries.all { it.priority == Logger.INFO && it.tag == "PRETTY_LOGGER" })
  }

  @Test fun combinesTagsWithoutDuplicatingTheGlobalTag() {
    val strategy = messageOnly().tag("app").build()
    strategy.log(Logger.DEBUG, "screen", "first")
    strategy.log(Logger.DEBUG, "app", "second")
    strategy.log(Logger.DEBUG, null, "third")
    assertEquals(9, output.entries.size)
    output.entries.forEachIndexed { index, entry ->
      assertEquals(if (index < 3) "app-screen" else "app", entry.tag)
    }
  }

  @Test fun preservesUnicodeMessages() {
    messageOnly().build().log(Logger.DEBUG, null, "日本語 • café 🙂")
    assertEquals(3, output.entries.size)
    assertEquals("│ 日本語 • café 🙂", output.entries[1].message)
  }

  @Test fun splitsLongMessagesWithoutLosingContent() {
    val message = "x".repeat(8050)
    messageOnly().build().log(Logger.ERROR, "large", message)
    assertEquals(5, output.entries.size)
    val chunks = output.entries.drop(1).dropLast(1)
    assertTrue(chunks.all { it.priority == Logger.ERROR && it.tag == "PRETTY_LOGGER-large" })
    assertTrue(chunks.all { it.message.startsWith("│ ") && it.message.substring(2).length <= 4000 })
    assertEquals(message, chunks.joinToString("") { it.message.substring(2) })
  }

  @Test fun keepsMultibyteCharactersIntactAcrossChunkBoundaries() {
    val message = "x" + "é🙂日".repeat(1000)
    messageOnly().build().log(Logger.DEBUG, null, message)
    val chunks = output.entries.drop(1).dropLast(1).map { it.message.substring(2) }
    assertTrue(chunks.size > 1)
    assertTrue(chunks.all { it.toByteArray(Charsets.UTF_8).size <= 4000 })
    assertEquals(message, chunks.joinToString(""))
  }

  @Test fun includesThreadInformationOnlyWhenEnabled() {
    messageOnly().showThreadInfo(true).build().log(Logger.DEBUG, null, "message")
    assertEquals(5, output.entries.size)
    assertEquals("│ Thread: ${Thread.currentThread().name}", output.entries[1].message)
    assertTrue(output.entries[2].message.startsWith("├"))
    assertEquals("│ message", output.entries[3].message)
  }

  @Test fun includesTheCallingMethodWithoutLoggerInternals() {
    val printer = LoggerPrinter()
    printer.addAdapter(AndroidLogAdapter(messageOnly().methodCount(1).build()))
    printer.d("message")
    assertEquals(5, output.entries.size)
    assertTrue(output.entries[1].message.contains(
        "PrettyFormatStrategyTest.includesTheCallingMethodWithoutLoggerInternals"))
    assertEquals("│ message", output.entries[3].message)
  }

  @Test fun boundsExcessiveMethodCountsAndOffsetsWithoutDroppingTheMessage() {
    for (offset in listOf(0, 10000)) {
      output.entries.clear()
      messageOnly().methodCount(10000).methodOffset(offset).build().log(Logger.DEBUG, null, "message")
      assertTrue(output.entries.first().message.startsWith("┌"))
      assertEquals("│ message", output.entries[output.entries.size - 2].message)
      assertTrue(output.entries.last().message.startsWith("└"))
    }
  }

  @Test fun rejectsNullMessagesFromJvmCallersBeforeWritingAnyOutput() {
    val error = assertThrows(InvocationTargetException::class.java) {
      PrettyFormatStrategy::class.java.getMethod("log", Int::class.javaPrimitiveType, String::class.java,
          String::class.java).invoke(messageOnly().build(), Logger.DEBUG, null, null)
    }
    assertTrue(error.targetException is NullPointerException)
    assertTrue(output.entries.isEmpty())
  }
}

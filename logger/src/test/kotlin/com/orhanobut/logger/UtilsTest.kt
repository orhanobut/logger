package com.orhanobut.logger

import org.junit.Assert.*
import org.junit.Test
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

class UtilsTest {
  @Test fun rendersThrowableStackTracesUsingTheJvm() {
    val error = IllegalStateException("failed", Exception("cause"))
    val expected = StringWriter()
    error.printStackTrace(PrintWriter(expected))
    assertEquals(expected.toString(), Utils.getStackTraceString(error))
  }

  @Test fun suppressesNullAndNestedUnknownHostExceptions() {
    assertEquals("", Utils.getStackTraceString(null))
    assertEquals("", Utils.getStackTraceString(UnknownHostException("offline")))
    assertEquals("", Utils.getStackTraceString(Exception("request failed",
        IllegalStateException(UnknownHostException("offline")))))
  }

  @Test fun rendersEveryPrimitiveArrayAndNestedObjectArrays() {
    val cases = listOf(
        booleanArrayOf(true, false) to "[true, false]",
        byteArrayOf(1, 2) to "[1, 2]",
        charArrayOf('a', 'b') to "[a, b]",
        shortArrayOf(1, 2) to "[1, 2]",
        intArrayOf(1, 2) to "[1, 2]",
        longArrayOf(1, 2) to "[1, 2]",
        floatArrayOf(1f, 2f) to "[1.0, 2.0]",
        doubleArrayOf(1.0, 2.0) to "[1.0, 2.0]",
        arrayOf(intArrayOf(1, 2), null) to "[[1, 2], null]",
        intArrayOf() to "[]",
        null to "null",
        "plain" to "plain",
    )
    for ((obj, expected) in cases) assertEquals(expected, Utils.toString(obj))
  }

  @Test fun namesEveryLogLevelAndHandlesUnknownLevels() {
    listOf("VERBOSE", "DEBUG", "INFO", "WARN", "ERROR", "ASSERT").forEachIndexed { index, level ->
      assertEquals(level, Utils.logLevel(index + 2))
    }
    assertEquals("UNKNOWN", Utils.logLevel(-1))
  }
}

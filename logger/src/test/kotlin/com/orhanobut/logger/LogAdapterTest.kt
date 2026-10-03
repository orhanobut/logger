package com.orhanobut.logger

import org.junit.Assert.*
import org.junit.Test
import java.lang.reflect.InvocationTargetException

class LogAdapterTest {
  @Test fun androidAndDiskAdaptersForwardToInjectedFormatStrategies() {
    val output = RecordingLog()
    val android = AndroidLogAdapter(output)
    val disk = DiskLogAdapter(output)
    assertTrue(android.isLoggable(Logger.INFO, null))
    assertTrue(disk.isLoggable(Logger.ERROR, "disk"))
    android.log(Logger.INFO, null, "android")
    disk.log(Logger.ERROR, "disk", "file")
    assertEquals(listOf(RecordingLog.Entry(Logger.INFO, null, "android"),
        RecordingLog.Entry(Logger.ERROR, "disk", "file")), output.entries)
  }

  @Test fun rejectsNullFormatStrategiesFromJvmCallers() {
    for (type in listOf(AndroidLogAdapter::class.java, DiskLogAdapter::class.java)) {
      val error = assertThrows(InvocationTargetException::class.java) {
        type.getConstructor(FormatStrategy::class.java).newInstance(null)
      }
      assertTrue(error.targetException is NullPointerException)
    }
  }
}

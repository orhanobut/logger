package com.orhanobut.logger

import org.junit.Before
import org.junit.Test
import org.mockito.Mock

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.initMocks

class LoggerTest {

  @Mock private lateinit var printer: Printer

  @Before fun setup() {
    initMocks(this)

    Logger.printer(printer)
  }

  @Test fun log() {
    val throwable = Throwable()
    Logger.log(Logger.VERBOSE, "tag", "message", throwable)

    verify(printer).log(Logger.VERBOSE, "tag", "message", throwable)
  }

  @Test fun debugLog() {
    Logger.d("message %s", "arg")

    verify(printer).d("message %s", "arg")
  }

  @Test fun verboseLog() {
    Logger.v("message %s", "arg")

    verify(printer).v("message %s", "arg")
  }

  @Test fun warningLog() {
    Logger.w("message %s", "arg")

    verify(printer).w("message %s", "arg")
  }

  @Test fun errorLog() {
    Logger.e("message %s", "arg")

    verify(printer).e(null as Throwable?, "message %s", "arg")
  }

  @Test fun errorLogWithThrowable() {
    val throwable = Throwable("throwable")
    Logger.e(throwable, "message %s", "arg")

    verify(printer).e(throwable, "message %s", "arg")
  }

  @Test fun infoLog() {
    Logger.i("message %s", "arg")

    verify(printer).i("message %s", "arg")
  }

  @Test fun wtfLog() {
    Logger.wtf("message %s", "arg")

    verify(printer).wtf("message %s", "arg")
  }

  @Test fun logObject() {
    val `object` = Any()
    Logger.d(`object`)

    verify(printer).d(`object`)
  }

  @Test fun jsonLog() {
    Logger.json("json")

    verify(printer).json("json")
  }

  @Test fun xmlLog() {
    Logger.xml("xml")

    verify(printer).xml("xml")
  }

  @Test fun oneTimeTag() {
    `when`(printer.t("tag")).thenReturn(printer)

    Logger.t("tag").d("message")

    verify(printer).t("tag")
    verify(printer).d("message")
  }

  @Test fun addAdapter() {
    val adapter = mock(LogAdapter::class.java)
    Logger.addLogAdapter(adapter)

    verify(printer).addAdapter(adapter)
  }

  @Test fun clearLogAdapters() {
    Logger.clearLogAdapters()

    verify(printer).clearLogAdapters()
  }
}

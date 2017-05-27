package com.orhanobut.logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoggerTest {

  @Mock Printer printer;

  @Before public void setup() {
    initMocks(this);

    Logger.printer(printer);
  }

  @Test public void log() {
    Throwable throwable = new Throwable();
    Logger.log(Logger.VERBOSE, "tag", "message", throwable);

    verify(printer).log(Logger.VERBOSE, "tag", "message", throwable);
  }

  @Test public void debugLog() {
    Logger.d("message %s", "arg");

    verify(printer).d("message %s", "arg");
  }

  @Test public void verboseLog() {
    Logger.v("message %s", "arg");

    verify(printer).v("message %s", "arg");
  }

  @Test public void warningLog() {
    Logger.w("message %s", "arg");

    verify(printer).w("message %s", "arg");
  }

  @Test public void errorLog() {
    Logger.e("message %s", "arg");

    verify(printer).e((Throwable) null, "message %s", "arg");
  }

  @Test public void errorLogWithThrowable() {
    Throwable throwable = new Throwable("throwable");
    Logger.e(throwable, "message %s", "arg");

    verify(printer).e(throwable, "message %s", "arg");
  }

  @Test public void infoLog() {
    Logger.i("message %s", "arg");

    verify(printer).i("message %s", "arg");
  }

  @Test public void wtfLog() {
    Logger.wtf("message %s", "arg");

    verify(printer).wtf("message %s", "arg");
  }

  @Test public void logObject() {
    Object object = new Object();
    Logger.d(object);

    verify(printer).d(object);
  }

  @Test public void jsonLog() {
    Logger.json("json");

    verify(printer).json("json");
  }

  @Test public void xmlLog() {
    Logger.xml("xml");

    verify(printer).xml("xml");
  }

  @Test public void oneTimeTag() {
    when(printer.t("tag")).thenReturn(printer);

    Logger.t("tag").d("message");

    verify(printer).t("tag");
    verify(printer).d("message");
  }

  @Test public void addAdapter() {
    LogAdapter adapter = mock(LogAdapter.class);
    Logger.addLogAdapter(adapter);

    verify(printer).addAdapter(adapter);
  }

  @Test public void clearLogAdapters() {
    Logger.clearLogAdapters();

    verify(printer).clearLogAdapters();
  }
}

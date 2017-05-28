package com.orhanobut.logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoggerPrinterTest {

  private final Printer printer = new LoggerPrinter();

  @Mock LogAdapter adapter;

  @Before public void setup() {
    initMocks(this);
    when(adapter.isLoggable(any(Integer.class), any(String.class))).thenReturn(true);
    printer.addAdapter(adapter);
  }

  @Test public void logDebug() {
    printer.d("message %s", "sent");

    verify(adapter).log(DEBUG, null, "message sent");
  }

  @Test public void logError() {
    printer.e("message %s", "sent");

    verify(adapter).log(ERROR, null, "message sent");
  }

  @Test public void logErrorWithThrowable() {
    Throwable throwable = new Throwable("exception");

    printer.e(throwable, "message %s", "sent");

    verify(adapter).log(eq(ERROR), isNull(String.class), contains("message sent : java.lang.Throwable: exception"));
  }

  @Test public void logWarning() {
    printer.w("message %s", "sent");

    verify(adapter).log(WARN, null, "message sent");
  }

  @Test public void logInfo() {
    printer.i("message %s", "sent");

    verify(adapter).log(INFO, null, "message sent");
  }

  @Test public void logWtf() {
    printer.wtf("message %s", "sent");

    verify(adapter).log(ASSERT, null, "message sent");
  }

  @Test public void logVerbose() {
    printer.v("message %s", "sent");

    verify(adapter).log(VERBOSE, null, "message sent");
  }

  @Test public void oneTimeTag() {
    printer.t("tag").d("message");

    verify(adapter).log(DEBUG, "tag", "message");
  }

  @Test public void logObject() {
    Object object = "Test";

    printer.d(object);

    verify(adapter).log(DEBUG, null, "Test");
  }

  @Test public void logArray() {
    Object object = new int[]{1, 6, 7, 30, 33};

    printer.d(object);

    verify(adapter).log(DEBUG, null, "[1, 6, 7, 30, 33]");
  }

  @Test public void logStringArray() {
    Object object = new String[]{"a", "b", "c"};

    printer.d(object);

    verify(adapter).log(DEBUG, null, "[a, b, c]");
  }

  @Test public void logMultiDimensionArray() {
    double[][] doubles = {
        {1, 6},
        {1.2, 33},
    };

    printer.d(doubles);

    verify(adapter).log(DEBUG, null, "[[1.0, 6.0], [1.2, 33.0]]");
  }

  @Test public void logList() {
    List<String> list = Arrays.asList("foo", "bar");
    printer.d(list);

    verify(adapter).log(DEBUG, null, list.toString());
  }

  @Test public void logMap() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map.put("key2", "value2");

    printer.d(map);

    verify(adapter).log(DEBUG, null, map.toString());
  }

  @Test public void logSet() {
    Set<String> set = new HashSet<>();
    set.add("key");
    set.add("key1");

    printer.d(set);

    verify(adapter).log(DEBUG, null, set.toString());
  }

  @Test public void logJsonObject() {
    printer.json("  {\"key\":3}");

    verify(adapter).log(DEBUG, null, "{\"key\": 3}");
  }

  @Test public void logJsonArray() {
    printer.json("[{\"key\":3}]");

    verify(adapter).log(DEBUG, null, "[{\"key\": 3}]");
  }


  @Test public void logInvalidJsonObject() {
    printer.json("no json");
    printer.json("{ missing end");

    verify(adapter, times(2)).log(ERROR, null, "Invalid Json");
  }

  @Test public void jsonLogEmptyOrNull() {
    printer.json(null);
    printer.json("");

    verify(adapter, times(2)).log(DEBUG, null, "Empty/Null json content");
  }

  @Test public void xmlLog() {
    printer.xml("<xml>Test</xml>");

    verify(adapter).log(DEBUG, null,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<xml>Test</xml>\n");
  }

  @Test public void invalidXmlLog() {
    printer.xml("xml>Test</xml>");

    verify(adapter).log(ERROR, null, "Invalid xml");
  }

  @Test public void xmlLogNullOrEmpty() {
    printer.xml(null);
    printer.xml("");

    verify(adapter, times(2)).log(DEBUG, null, "Empty/Null xml content");
  }

  @Test public void clearLogAdapters() {
    printer.clearLogAdapters();

    printer.d("");

    verifyZeroInteractions(adapter);
  }

  @Test public void addAdapter() {
    printer.clearLogAdapters();
    LogAdapter adapter1 = mock(LogAdapter.class);
    LogAdapter adapter2 = mock(LogAdapter.class);

    printer.addAdapter(adapter1);
    printer.addAdapter(adapter2);

    printer.d("message");

    verify(adapter1).isLoggable(DEBUG, null);
    verify(adapter2).isLoggable(DEBUG, null);
  }

  @Test public void doNotLogIfNotLoggable() {
    printer.clearLogAdapters();
    LogAdapter adapter1 = mock(LogAdapter.class);
    when(adapter1.isLoggable(DEBUG, null)).thenReturn(false);

    LogAdapter adapter2 = mock(LogAdapter.class);
    when(adapter2.isLoggable(DEBUG, null)).thenReturn(true);

    printer.addAdapter(adapter1);
    printer.addAdapter(adapter2);

    printer.d("message");

    verify(adapter1, never()).log(DEBUG, null, "message");
    verify(adapter2).log(DEBUG, null, "message");
  }

  @Test public void logWithoutMessageAndThrowable() {
    printer.log(DEBUG, null, null, null);

    verify(adapter).log(DEBUG, null, "Empty/NULL log message");
  }

  @Test public void logWithOnlyThrowableWithoutMessage() {
    Throwable throwable = new Throwable("exception");
    printer.log(DEBUG, null, null, throwable);

    verify(adapter).log(eq(DEBUG), isNull(String.class), contains("java.lang.Throwable: exception"));
  }
}
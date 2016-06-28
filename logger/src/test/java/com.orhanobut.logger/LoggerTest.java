package com.orhanobut.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoggerTest {

  private static final String DEFAULT_TAG = "PRETTYLOGGER";

  String threadName;

  @Before public void setup() {
    threadName = Thread.currentThread().getName();

    Logger.init();
  }

  @After public void tearDown() {
    Logger.resetSettings();
  }

  @Test public void testLog() {
    Logger.log(DEBUG, null, "message", null);
    assertLog(DEBUG).hasMessageWithDefaultSettings("message");

    Logger.log(DEBUG, "tag", "message", null);
    assertLog(DEFAULT_TAG + "-tag", DEBUG).hasMessageWithDefaultSettings("message");
  }

  @Ignore
  @Test public void testLogThrowable() {
    Throwable throwable = new Throwable("throwable");
    String stackString = "message : " + Helper.getStackTraceString(throwable);
    String[] stackItems = stackString.split("\\n");
    Logger.log(DEBUG, null, "message", throwable);

    assertLog(DEBUG).hasMessageWithDefaultSettings(stackItems);
  }

  @Test public void debugLog() {
    Logger.d("message");

    assertLog(DEBUG).hasMessageWithDefaultSettings("message");
  }

  @Test public void verboseLog() {
    Logger.v("message");

    assertLog(VERBOSE).hasMessageWithDefaultSettings("message");
  }

  @Test public void warningLog() {
    Logger.w("message");

    assertLog(WARN).hasMessageWithDefaultSettings("message");
  }

  @Test public void errorLog() {
    Logger.e("message");

    assertLog(ERROR).hasMessageWithDefaultSettings("message");
  }

  @Ignore("Through Terminal somehow not working, but on Studio it works, needs investigation")
  @Test public void errorLogWithThrowable() {
    Throwable throwable = new Throwable("throwable");
    Logger.e(throwable, "message");

    String stackString = "message : " + Helper.getStackTraceString(throwable);
    String[] stackItems = stackString.split("\\n");

    assertLog(ERROR).hasMessageWithDefaultSettings(stackItems);
  }

  @Ignore("Through Terminal somehow not working, but on Studio it works, needs investigation")
  @Test public void errorLogWithThrowableWithoutMessage() {
    Throwable throwable = new Throwable("throwable");
    Logger.e(throwable, null);

    String stackString = Helper.getStackTraceString(throwable);
    String[] stackItems = stackString.split("\\n");

    assertLog(ERROR).hasMessageWithDefaultSettings(stackItems);
  }

  @Ignore("Through Terminal somehow not working, but on Studio it works, needs investigation")
  @Test public void errorLogNoThrowableNoMessage() {
    Logger.e(null, null);

    assertLog(ERROR).hasMessageWithDefaultSettings("No message/exception is set");
  }

  @Test public void infoLog() {
    Logger.i("message");

    assertLog(INFO).hasMessageWithDefaultSettings("message");
  }

  @Test public void wtfLog() {
    Logger.wtf("message");

    assertLog(ASSERT).hasMessageWithDefaultSettings("message");
  }

  @Test public void logArray() {
    double[][] doubles = {{1.2, 1.6, 1.7, 30, 33},
        {1.2, 1.6, 1.7, 30, 33},
        {1.2, 1.6, 1.7, 30, 33},
        {1.2, 1.6, 1.7, 30, 33}};

    Logger.d(doubles);

    String message = Arrays.deepToString(doubles);
    assertLog(DEBUG).hasMessageWithDefaultSettings(message);
  }

  @Test public void logList() {
    List<String> list = Arrays.asList("foo", "bar");
    Logger.d(list);

    assertLog(DEBUG).hasMessageWithDefaultSettings(list.toString());
  }

  @Test public void logMap() {
    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map.put("key2", "value2");

    Logger.d(map);

    assertLog(DEBUG).hasMessageWithDefaultSettings(map.toString());
  }

  @Test public void logSet() {
    Set<String> set = new HashSet<>();
    set.add("key");
    set.add("key1");

    Logger.d(set);

    assertLog(DEBUG).hasMessageWithDefaultSettings(set.toString());
  }

  @Test public void jsonLObjectLog() {
    String[] messages = new String[]{"{", "  \"key\": 3", "}"};

    Logger.json("  {\"key\":3}");

    assertLog(DEBUG).hasMessageWithDefaultSettings(messages);
  }

  @Test public void jsonArrayLog() {
    String[] messages = new String[]{
        "[",
        "  {",
        "    \"key\": 3",
        "  }",
        "]"
    };

    Logger.json("[{\"key\":3}]");

    assertLog(DEBUG).hasMessageWithDefaultSettings(messages);
  }

  @Test public void testInvalidJsonLog() {
    Logger.json("no json");
    assertLog(ERROR).hasMessageWithDefaultSettings("Invalid Json");

    Logger.json("{ missing end");
    assertLog(ERROR).hasMessageWithDefaultSettings("Invalid Json");
  }

  @Test public void jsonLogEmptyOrNull() {
    Logger.json(null);
    assertLog(DEBUG).hasMessageWithDefaultSettings("Empty/Null json content");

    Logger.json("");
    assertLog(DEBUG).hasMessageWithDefaultSettings("Empty/Null json content");
  }

  @Test public void xmlLog() {
    String[] messages = new String[]{
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
        "<xml>Test</xml>"
    };

    Logger.xml("<xml>Test</xml>");

    assertLog(DEBUG).hasMessageWithDefaultSettings(messages);
  }

  @Test public void invalidXmlLog() {
    Logger.xml("xml>Test</xml>");

    assertLog(ERROR).hasMessageWithDefaultSettings("Invalid xml");
  }

  @Test public void xmlLogNullOrEmpty() {
    Logger.xml(null);
    assertLog(DEBUG).hasMessageWithDefaultSettings("Empty/Null xml content");

    Logger.xml("");
    assertLog(DEBUG).hasMessageWithDefaultSettings("Empty/Null xml content");
  }

  @Test public void logWithoutThread() {
    Logger.init().hideThreadInfo();
    Logger.i("message");
    assertLog(INFO)
        .hasTopBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithCustomTag() {
    Logger.init("CustomTag");
    Logger.i("message");
    assertLog("CustomTag", INFO)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOneMethodInfo() {
    Logger.init().methodCount(1);
    Logger.i("message");
    assertLog(INFO)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithNoMethodInfo() {
    Logger.init().methodCount(0);
    Logger.i("message");

    assertLog(INFO)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithNoMethodInfoAndNoThreadInfo() {
    Logger.init().methodCount(0).hideThreadInfo();
    Logger.i("message");

    assertLog(INFO)
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyOnceCustomTag() {
    Logger.init().hideThreadInfo().methodCount(0);
    Logger.t("CustomTag").i("message");
    Logger.i("message");

    assertLog("PRETTYLOGGER-CustomTag", INFO)
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .defaultTag()
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyOnceMethodInfo() {
    Logger.init().hideThreadInfo().methodCount(0);
    Logger.t(1).i("message");
    Logger.i("message");

    assertLog(INFO)
        .hasTopBorder()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyOnceMethodInfoAndCustomTag() {
    Logger.init().hideThreadInfo().methodCount(0);
    Logger.t("CustomTag", 1).i("message");
    Logger.i("message");

    assertLog("PRETTYLOGGER-CustomTag", INFO)
        .hasTopBorder()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .defaultTag()
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logNone() {
    Logger.init().logLevel(LogLevel.NONE);
    Logger.i("message");

    assertLog(INFO)
        .hasNoMoreMessages();
  }

  @Test public void useDefaultSettingsIfInitNotCalled() {
    Logger.i("message");
    assertLog(INFO)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  private static LogAssert assertLog(int priority) {
    return assertLog(null, priority);
  }

  private static LogAssert assertLog(String tag, int priority) {
    return new LogAssert(ShadowLog.getLogs(), tag, priority);
  }

}

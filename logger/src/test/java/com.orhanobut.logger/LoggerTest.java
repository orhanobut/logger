package com.orhanobut.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLog.LogItem;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoggerTest {

  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;
  public static final int INFO = 4;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;

  String threadName;

  @Before public void setup() {
    threadName = Thread.currentThread().getName();

    Logger.init();
  }

  @After public void tearDown() {
    Logger.clear();
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

  private static final class LogAssert {
    private static final String DEFAULT_TAG = "PRETTYLOGGER";

    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private final List<LogItem> items;
    private final int priority;

    private String tag;

    private int index = 0;

    private LogAssert(List<LogItem> items, String tag, int priority) {
      this.items = items;
      this.tag = tag == null ? DEFAULT_TAG : tag;
      this.priority = priority;
    }

    public LogAssert hasTopBorder() {
      return hasLog(priority, tag, TOP_BORDER);
    }

    public LogAssert hasBottomBorder() {
      return hasLog(priority, tag, BOTTOM_BORDER);
    }

    public LogAssert hasMiddleBorder() {
      return hasLog(priority, tag, MIDDLE_BORDER);
    }

    public LogAssert hasThread(String threadName) {
      return hasLog(priority, tag, HORIZONTAL_DOUBLE_LINE + " " + "Thread: " + threadName);
    }

    public LogAssert hasMethodInfo(String methodInfo) {
      return hasLog(priority, tag, HORIZONTAL_DOUBLE_LINE + " " + methodInfo);
    }

    public LogAssert hasMessage(String message) {
      return hasLog(priority, tag, HORIZONTAL_DOUBLE_LINE + " " + message);
    }

    private LogAssert hasLog(int priority, String tag, String message) {
      LogItem item = items.get(index++);
      assertThat(item.type).isEqualTo(priority);
      assertThat(item.tag).isEqualTo(tag);
      assertThat(item.msg).isEqualTo(message);
      return this;
    }

    public LogAssert skip() {
      index++;
      return this;
    }

    public LogAssert defaultTag() {
      tag = DEFAULT_TAG;
      return this;
    }

    public void hasNoMoreMessages() {
      assertThat(items).hasSize(index);
      ShadowLog.getLogs().clear();
    }

    public LogAssert hasMessageWithDefaultSettings(String... messages) {
      hasTopBorder();
      skip();
      hasMiddleBorder();
      skip();
      skip();
      hasMiddleBorder();

      for (String message : messages) {
        hasMessage(message);
      }

      hasBottomBorder();
      hasNoMoreMessages();

      return this;
    }
  }
}

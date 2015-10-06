package com.orhanobut.logger;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowLog.LogItem;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LoggerTest {

  public static final int DEBUG = 3;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;
  public static final int INFO = 4;
  public static final int VERBOSE = 2;
  public static final int WARN = 5;

  @After public void tearDown() {
    Logger.clear();
  }

  @Test public void debugLog() {
    Logger.init();
    Logger.d("message");
    assertLog(DEBUG, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void verboseLog() {
    Logger.init();
    Logger.v("message");
    assertLog(VERBOSE, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void warningLog() {
    Logger.init();
    Logger.w("message");
    assertLog(WARN, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void errorLog() {
    Logger.init();
    Logger.e("message");
    assertLog(ERROR, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void infoLog() {
    Logger.init();
    Logger.i("message");
    assertLog(INFO, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void wtfLog() {
    Logger.init();
    Logger.wtf("message");
    assertLog(ASSERT, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithoutThread() {
    Logger.init().hideThreadInfo();
    Logger.i("message");
    assertLog(INFO, "message")
        .hasTopBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithCustomTag() {
    Logger.init("CustomTag");
    Logger.i("message");
    assertLog("CustomTag", INFO, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOneMethodInfo() {
    Logger.init().methodCount(1);
    Logger.i("message");
    assertLog(INFO, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithNoMethodInfo() {
    Logger.init().methodCount(0);
    Logger.i("message");

    assertLog(INFO, "message")
        .hasTopBorder()
        .hasThread("Test worker")
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithNoMethodInfoAndNoThreadInfo() {
    Logger.init().methodCount(0).hideThreadInfo();
    Logger.i("message");

    assertLog(INFO, "message")
        .hasTopBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyOnceCustomTag() {
    Logger.init().hideThreadInfo().methodCount(0);
    Logger.t("CustomTag").i("message");
    Logger.i("message");

    assertLog("PRETTYLOGGER-CustomTag", INFO, "message")
        .hasTopBorder()
        .hasMessage()
        .hasBottomBorder()
        .defaultTag()
        .hasTopBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyOnceMethodInfo() {
    Logger.init().hideThreadInfo().methodCount(0);
    Logger.t(1).i("message");
    Logger.i("message");

    assertLog(INFO, "message")
        .hasTopBorder()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasTopBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyOnceMethodInfoAndCustomTag() {
    Logger.init().hideThreadInfo().methodCount(0);
    Logger.t("CustomTag", 1).i("message");
    Logger.i("message");

    assertLog("PRETTYLOGGER-CustomTag", INFO, "message")
        .hasTopBorder()
        .skip()
        .hasMiddleBorder()
        .hasMessage()
        .hasBottomBorder()
        .defaultTag()
        .hasTopBorder()
        .hasMessage()
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logNone() {
    Logger.init().logLevel(LogLevel.NONE);
    Logger.i("message");

    assertLog(INFO, "message")
        .hasNoMoreMessages();
  }

  private static LogAssert assertLog(int priority, String message) {
    return assertLog(null, priority, message);
  }

  private static LogAssert assertLog(String tag, int priority, String message) {
    return new LogAssert(ShadowLog.getLogs(), tag, priority, message);
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
    private final String message;
    private final int priority;

    private String tag;

    private int index = 0;

    private LogAssert(List<LogItem> items, String tag, int priority, String message) {
      this.items = items;
      this.tag = tag == null ? DEFAULT_TAG : tag;
      this.priority = priority;
      this.message = message;
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

    public LogAssert hasMessage() {
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
    }
  }
}

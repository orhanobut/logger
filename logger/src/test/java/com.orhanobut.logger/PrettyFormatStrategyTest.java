package com.orhanobut.logger;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.orhanobut.logger.Logger.DEBUG;

public class PrettyFormatStrategyTest {

  private final String threadName = Thread.currentThread().getName();
  private final MockLogStrategy logStrategy = new MockLogStrategy();
  private final PrettyFormatStrategy.Builder builder =
      PrettyFormatStrategy.newBuilder().logStrategy(logStrategy);

  //TODO: Check the actual method info
  @Test public void defaultLog() {
    FormatStrategy formatStrategy = builder.build();

    formatStrategy.log(DEBUG, null, "message");

    assertLog(DEBUG)
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

  @Test public void logWithoutThreadInfo() {
    FormatStrategy formatStrategy = builder.showThreadInfo(false).build();

    formatStrategy.log(DEBUG, null, "message");

    assertLog(DEBUG)
        .hasTopBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithoutMethodInfo() {
    FormatStrategy formatStrategy = builder.methodCount(0).build();

    formatStrategy.log(DEBUG, null, "message");

    assertLog(DEBUG)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithOnlyMessage() {
    FormatStrategy formatStrategy = builder
        .methodCount(0)
        .showThreadInfo(false)
        .build();

    formatStrategy.log(DEBUG, null, "message");

    assertLog(DEBUG)
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  //TODO: Check the actual method info
  @Test public void logWithCustomMethodOffset() {
    FormatStrategy formatStrategy = builder
        .methodOffset(2)
        .showThreadInfo(false)
        .build();

    formatStrategy.log(DEBUG, null, "message");

    assertLog(DEBUG)
        .hasTopBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  @Test public void logWithCustomTag() {
    FormatStrategy formatStrategy = builder
        .tag("custom")
        .build();

    formatStrategy.log(DEBUG, null, "message");

    assertLog("custom", DEBUG)
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

  @Test public void logWithOneTimeTag() {
    FormatStrategy formatStrategy = builder
        .tag("custom")
        .build();

    formatStrategy.log(DEBUG, "tag", "message");

    assertLog("custom-tag", DEBUG)
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

  // TODO: assert values, for now this checks that Logger doesn't crash
  @Test public void logWithExceedingMethodCount() {
    FormatStrategy formatStrategy = builder
        .methodCount(50)
        .build();

    formatStrategy.log(DEBUG, null, "message");
  }

  @Test public void logWithBigChunk() {
    FormatStrategy formatStrategy = builder.build();

    StringBuilder chunk1 = new StringBuilder();
    for (int i = 0; i < 400; i++) {
      chunk1.append("1234567890");
    }
    StringBuilder chunk2 = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      chunk2.append("ABCDEFGD");
    }

    formatStrategy.log(DEBUG, null, chunk1.toString() + chunk2.toString());

    assertLog(DEBUG)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage(chunk1.toString())
        .hasMessage(chunk2.toString())
        .hasBottomBorder()
        .hasNoMoreMessages();
  }

  private static class MockLogStrategy implements LogStrategy {
    List<LogAssert.LogItem> logItems = new ArrayList<>();

    @Override public void log(int priority, String tag, String message) {
      logItems.add(new LogAssert.LogItem(priority, tag, message));
    }
  }

  private LogAssert assertLog(int priority) {
    return assertLog(null, priority);
  }

  private LogAssert assertLog(String tag, int priority) {
    return new LogAssert(logStrategy.logItems, tag, priority);
  }
}

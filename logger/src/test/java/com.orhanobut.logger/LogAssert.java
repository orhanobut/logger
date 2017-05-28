package com.orhanobut.logger;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

final class LogAssert {
  private static final String DEFAULT_TAG = "PRETTY_LOGGER";

  private static final char TOP_LEFT_CORNER = '┌';
  private static final char BOTTOM_LEFT_CORNER = '└';
  private static final char MIDDLE_CORNER = '├';
  private static final char HORIZONTAL_LINE = '│';
  private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
  private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
  private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

  private final List<LogItem> items;
  private final int priority;

  private String tag;

  private int index = 0;

  LogAssert(List<LogItem> items, String tag, int priority) {
    this.items = items;
    this.tag = tag == null ? DEFAULT_TAG : tag;
    this.priority = priority;
  }

  LogAssert hasTopBorder() {
    return hasLog(priority, tag, TOP_BORDER);
  }

  LogAssert hasBottomBorder() {
    return hasLog(priority, tag, BOTTOM_BORDER);
  }

  LogAssert hasMiddleBorder() {
    return hasLog(priority, tag, MIDDLE_BORDER);
  }

  LogAssert hasThread(String threadName) {
    return hasLog(priority, tag, HORIZONTAL_LINE + " " + "Thread: " + threadName);
  }

  LogAssert hasMethodInfo(String methodInfo) {
    return hasLog(priority, tag, HORIZONTAL_LINE + " " + methodInfo);
  }

  LogAssert hasMessage(String message) {
    return hasLog(priority, tag, HORIZONTAL_LINE + " " + message);
  }

  private LogAssert hasLog(int priority, String tag, String message) {
    LogItem item = items.get(index++);
    assertThat(item.priority).isEqualTo(priority);
    assertThat(item.tag).isEqualTo(tag);
    assertThat(item.message).isEqualTo(message);
    return this;
  }

  LogAssert skip() {
    index++;
    return this;
  }

  void hasNoMoreMessages() {
    assertThat(items).hasSize(index);
    items.clear();
  }

  static class LogItem {
    final int priority;
    final String tag;
    final String message;

    LogItem(int priority, String tag, String message) {
      this.priority = priority;
      this.tag = tag;
      this.message = message;
    }
  }
}

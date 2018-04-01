package com.orhanobut.logger

import com.google.common.truth.Truth.assertThat

internal class LogAssert(private val items: MutableList<LogItem>, tag: String?, private val priority: Int) {

  private val tag: String

  private var index = 0

  init {
    this.tag = tag ?: DEFAULT_TAG
  }

  fun hasTopBorder(): LogAssert {
    return hasLog(priority, tag, TOP_BORDER)
  }

  fun hasBottomBorder(): LogAssert {
    return hasLog(priority, tag, BOTTOM_BORDER)
  }

  fun hasMiddleBorder(): LogAssert {
    return hasLog(priority, tag, MIDDLE_BORDER)
  }

  fun hasThread(threadName: String): LogAssert {
    return hasLog(priority, tag, "$HORIZONTAL_LINE Thread: $threadName")
  }

  fun hasMethodInfo(methodInfo: String): LogAssert {
    return hasLog(priority, tag, "$HORIZONTAL_LINE $methodInfo")
  }

  fun hasMessage(message: String): LogAssert {
    return hasLog(priority, tag, "$HORIZONTAL_LINE $message")
  }

  private fun hasLog(priority: Int, tag: String, message: String): LogAssert = apply {
    val item = items[index++]
    assertThat(item.priority).isEqualTo(priority)
    assertThat(item.tag).isEqualTo(tag)
    assertThat(item.message).isEqualTo(message)
  }

  fun skip(): LogAssert = apply {
    index++
  }

  fun hasNoMoreMessages() {
    assertThat(items).hasSize(index)
    items.clear()
  }

  internal class LogItem(val priority: Int, val tag: String, val message: String)

  companion object {
    private const val DEFAULT_TAG = "PRETTY_LOGGER"

    private const val TOP_LEFT_CORNER = '┌'
    private const val BOTTOM_LEFT_CORNER = '└'
    private const val MIDDLE_CORNER = '├'
    private const val HORIZONTAL_LINE = '│'
    private const val DOUBLE_DIVIDER = "────────────────────────────────────────────────────────"
    private const val SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    private val TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    private val BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    private val MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER
  }
}

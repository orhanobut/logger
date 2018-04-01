package com.orhanobut.logger

import org.junit.Test

import java.util.ArrayList

import com.orhanobut.logger.Logger.DEBUG

class PrettyFormatStrategyTest {

  private val threadName = Thread.currentThread().name
  private val logStrategy = MockLogStrategy()
  private val builder = PrettyFormatStrategy.newBuilder().logStrategy(logStrategy)

  //TODO: Check the actual method info
  @Test fun defaultLog() {
    val formatStrategy = builder.build()

    formatStrategy.log(DEBUG, null, "message")

    assertLog(DEBUG)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  @Test fun logWithoutThreadInfo() {
    val formatStrategy = builder.showThreadInfo(false).build()

    formatStrategy.log(DEBUG, null, "message")

    assertLog(DEBUG)
        .hasTopBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  @Test fun logWithoutMethodInfo() {
    val formatStrategy = builder.methodCount(0).build()

    formatStrategy.log(DEBUG, null, "message")

    assertLog(DEBUG)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  @Test fun logWithOnlyMessage() {
    val formatStrategy = builder
        .methodCount(0)
        .showThreadInfo(false)
        .build()

    formatStrategy.log(DEBUG, null, "message")

    assertLog(DEBUG)
        .hasTopBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  //TODO: Check the actual method info
  @Test fun logWithCustomMethodOffset() {
    val formatStrategy = builder
        .methodOffset(2)
        .showThreadInfo(false)
        .build()

    formatStrategy.log(DEBUG, null, "message")

    assertLog(DEBUG)
        .hasTopBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  @Test fun logWithCustomTag() {
    val formatStrategy = builder
        .tag("custom")
        .build()

    formatStrategy.log(DEBUG, null, "message")

    assertLog("custom", DEBUG)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  @Test fun logWithOneTimeTag() {
    val formatStrategy = builder
        .tag("custom")
        .build()

    formatStrategy.log(DEBUG, "tag", "message")

    assertLog("custom-tag", DEBUG)
        .hasTopBorder()
        .hasThread(threadName)
        .hasMiddleBorder()
        .skip()
        .skip()
        .hasMiddleBorder()
        .hasMessage("message")
        .hasBottomBorder()
        .hasNoMoreMessages()
  }

  // TODO: assert values, for now this checks that Logger doesn't crash
  @Test fun logWithExceedingMethodCount() {
    val formatStrategy = builder
        .methodCount(50)
        .build()

    formatStrategy.log(DEBUG, null, "message")
  }

  @Test fun logWithBigChunk() {
    val formatStrategy = builder.build()

    val chunk1 = StringBuilder()
    for (i in 0..399) {
      chunk1.append("1234567890")
    }
    val chunk2 = StringBuilder()
    for (i in 0..9) {
      chunk2.append("ABCDEFGD")
    }

    formatStrategy.log(DEBUG, null, chunk1.toString() + chunk2.toString())

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
        .hasNoMoreMessages()
  }

  private class MockLogStrategy : LogStrategy {
    internal var logItems: MutableList<LogAssert.LogItem> = ArrayList()

    override fun log(priority: Int, tag: String?, message: String) {
      logItems.add(LogAssert.LogItem(priority, tag ?: "", message))
    }
  }

  private fun assertLog(priority: Int): LogAssert {
    return assertLog(null, priority)
  }

  private fun assertLog(tag: String?, priority: Int): LogAssert {
    return LogAssert(logStrategy.logItems, tag, priority)
  }
}

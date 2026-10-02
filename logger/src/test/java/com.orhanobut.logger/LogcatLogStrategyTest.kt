package com.orhanobut.logger

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

import com.google.common.truth.Truth.assertThat
import com.orhanobut.logger.Logger.DEBUG

@RunWith(RobolectricTestRunner::class)
class LogcatLogStrategyTest {

  @Test fun log() {
    val logStrategy = LogcatLogStrategy()

    logStrategy.log(DEBUG, "tag", "message")

    val logItems = ShadowLog.getLogsForTag("tag")
    assertThat(logItems).hasSize(1)
    assertThat(logItems[0].type).isEqualTo(DEBUG)
    assertThat(logItems[0].msg).isEqualTo("message")
    assertThat(logItems[0].tag).isEqualTo("tag")
  }

  @Test fun logWithNullTag() {
    val logStrategy = LogcatLogStrategy()

    logStrategy.log(DEBUG, null, "message")

    val logItems = ShadowLog.getLogsForTag(LogcatLogStrategy.DEFAULT_TAG)
    assertThat(logItems).hasSize(1)
    assertThat(logItems[0].tag).isEqualTo(LogcatLogStrategy.DEFAULT_TAG)
  }

}

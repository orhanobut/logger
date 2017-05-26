package com.orhanobut.logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.orhanobut.logger.Logger.DEBUG;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class LogcatLogStrategyTest {

  @Test public void log() {
    LogStrategy logStrategy = new LogcatLogStrategy();

    logStrategy.log(DEBUG, "tag", "message");

    List<ShadowLog.LogItem> logItems = ShadowLog.getLogs();
    assertThat(logItems.get(0).type).isEqualTo(DEBUG);
    assertThat(logItems.get(0).msg).isEqualTo("message");
    assertThat(logItems.get(0).tag).isEqualTo("tag");
  }

}
package com.orhanobut.logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SettingsTest {

  private Settings settings;

  @Before public void setup() {
    settings = new Settings();
  }

  @After public void tearDown() {
    settings = null;
  }

  @Test public void testDefaultShowThreadInfo() {
    assertThat(settings.isShowThreadInfo()).isTrue();
  }

  @Test public void testCustomShowThreadInfo() {
    settings.hideThreadInfo();
    assertThat(settings.isShowThreadInfo()).isFalse();
  }

  @Test public void testDefaultLogLevel() {
    assertThat(settings.getLogLevel()).isEqualTo(LogLevel.FULL);
  }

  @Test public void testCustomLogLevel() {
    settings.logLevel(LogLevel.NONE);
    assertThat(settings.getLogLevel()).isEqualTo(LogLevel.NONE);

    settings.logLevel(LogLevel.FULL);
    assertThat(settings.getLogLevel()).isEqualTo(LogLevel.FULL);
  }

  @Test public void testMethodCount() {
    //default
    assertThat(settings.getMethodCount()).isEqualTo(2);

    settings.methodCount(4);
    assertThat(settings.getMethodCount()).isEqualTo(4);

    //negative values should be convert to 0
    settings.methodCount(-10);
    assertThat(settings.getMethodCount()).isEqualTo(0);
  }

  @Test public void testMethodOffset() {
    //default
    assertThat(settings.getMethodOffset()).isEqualTo(0);

    settings.methodOffset(10);
    assertThat(settings.getMethodOffset()).isEqualTo(10);

    settings.methodOffset(-10);
    assertThat(settings.getMethodOffset()).isEqualTo(-10);
  }
}

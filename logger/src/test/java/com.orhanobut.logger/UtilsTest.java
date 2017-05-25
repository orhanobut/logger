package com.orhanobut.logger;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.net.UnknownHostException;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class)
public class UtilsTest {

  @Test public void isEmpty() {
    assertThat(Utils.isEmpty("")).isTrue();
    assertThat(Utils.isEmpty(null)).isTrue();
  }

  @Test public void equals() {
    assertThat(Utils.equals("a", "a")).isTrue();
    assertThat(Utils.equals("as", "b")).isFalse();
    assertThat(Utils.equals(null, "b")).isFalse();
    assertThat(Utils.equals("a", null)).isFalse();
  }

  @Test public void getStackTraceString() {
    Throwable throwable = new Throwable("test");
    String androidTraceString = Log.getStackTraceString(throwable);
    assertThat(Utils.getStackTraceString(throwable)).isEqualTo(androidTraceString);
  }

  @Test public void getStackTraceStringReturnsEmptyStringWithNull() {
    assertThat(Utils.getStackTraceString(null)).isEqualTo("");
  }

  @Test public void getStackTraceStringReturnEmptyStringWithUnknownHostException() {
    assertThat(Utils.getStackTraceString(new UnknownHostException())).isEqualTo("");
  }

  @Test public void shouldLogReturnsTrueForHigherPriorityThanLogLevel() {
    assertThat(Utils.isLoggable(LogLevel.DEBUG, LogLevel.WARN)).isEqualTo(true);
  }

  @Test public void shouldLogReturnsTrueForEqualPriorityAndLogLevel() {
    assertThat(Utils.isLoggable(LogLevel.DEBUG, LogLevel.DEBUG)).isEqualTo(true);
  }

  @Test public void shouldLogReturnsFalseForHigherLogLevelThanPriority() {
    assertThat(Utils.isLoggable(LogLevel.WARN, LogLevel.DEBUG)).isEqualTo(false);
  }
}
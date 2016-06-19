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
public class HelperTest {

  @Test public void isEmpty() {
    assertThat(Helper.isEmpty("")).isTrue();
    assertThat(Helper.isEmpty(null)).isTrue();
  }

  @Test public void equals() {
    assertThat(Helper.equals("a", "a")).isTrue();
    assertThat(Helper.equals("as", "b")).isFalse();
    assertThat(Helper.equals(null, "b")).isFalse();
    assertThat(Helper.equals("a", null)).isFalse();
  }

  @Test public void getStackTraceString() {
    Throwable throwable = new Throwable("test");
    String androidTraceString = Log.getStackTraceString(throwable);
    assertThat(Helper.getStackTraceString(throwable)).isEqualTo(androidTraceString);
  }

  @Test public void getStackTraceStringReturnsEmptyStringWithNull() {
    assertThat(Helper.getStackTraceString(null)).isEqualTo("");
  }

  @Test public void getStackTraceStringReturnEmptyStringWithUnknownHostException() {
    assertThat(Helper.getStackTraceString(new UnknownHostException())).isEqualTo("");
  }
}
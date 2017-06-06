package com.orhanobut.logger;

import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.UnknownHostException;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
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

  @Test public void logLevels() {
    assertThat(Utils.logLevel(Logger.DEBUG)).isEqualTo("DEBUG");
    assertThat(Utils.logLevel(Logger.WARN)).isEqualTo("WARN");
    assertThat(Utils.logLevel(Logger.INFO)).isEqualTo("INFO");
    assertThat(Utils.logLevel(Logger.VERBOSE)).isEqualTo("VERBOSE");
    assertThat(Utils.logLevel(Logger.ASSERT)).isEqualTo("ASSERT");
    assertThat(Utils.logLevel(Logger.ERROR)).isEqualTo("ERROR");
    assertThat(Utils.logLevel(100)).isEqualTo("UNKNOWN");
  }

  @Test public void objectToString() {
    Object object = "Test";

    assertThat(Utils.toString(object)).isEqualTo("Test");
  }

  @Test public void toStringWithNull() {
    assertThat(Utils.toString(null)).isEqualTo("null");
  }

  @Test public void primitiveArrayToString() {
    Object booleanArray = new boolean[]{true, false, true};
    assertThat(Utils.toString(booleanArray)).isEqualTo("[true, false, true]");

    Object byteArray = new byte[]{1, 0, 1};
    assertThat(Utils.toString(byteArray)).isEqualTo("[1, 0, 1]");

    Object charArray = new char[]{'a', 'b', 'c'};
    assertThat(Utils.toString(charArray)).isEqualTo("[a, b, c]");

    Object shortArray = new short[]{1, 3, 5};
    assertThat(Utils.toString(shortArray)).isEqualTo("[1, 3, 5]");

    Object intArray = new int[]{1, 3, 5};
    assertThat(Utils.toString(intArray)).isEqualTo("[1, 3, 5]");

    Object longArray = new long[]{1, 3, 5};
    assertThat(Utils.toString(longArray)).isEqualTo("[1, 3, 5]");

    Object floatArray = new float[]{1, 3, 5};
    assertThat(Utils.toString(floatArray)).isEqualTo("[1.0, 3.0, 5.0]");

    Object doubleArray = new double[]{1, 3, 5};
    assertThat(Utils.toString(doubleArray)).isEqualTo("[1.0, 3.0, 5.0]");
  }

  @Test public void multiDimensionArrayToString() {
    Object object = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };

    assertThat(Utils.toString(object)).isEqualTo("[[1, 2, 3], [4, 5, 6]]");
  }
}
package com.orhanobut.logger

import android.util.Log

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import java.net.UnknownHostException

import com.google.common.truth.Truth.assertThat

@RunWith(RobolectricTestRunner::class)
class UtilsTest {

  @Test fun isEmpty() {
    assertThat(Utils.isEmpty("")).isTrue()
    assertThat(Utils.isEmpty(null)).isTrue()
  }

  @Test fun equals() {
    assertThat(Utils.equals("a", "a")).isTrue()
    assertThat(Utils.equals("as", "b")).isFalse()
    assertThat(Utils.equals(null, "b")).isFalse()
    assertThat(Utils.equals("a", null)).isFalse()
  }

  @Test fun getStackTraceString() {
    val throwable = Throwable("test")
    val androidTraceString = Log.getStackTraceString(throwable)
    assertThat(Utils.getStackTraceString(throwable)).isEqualTo(androidTraceString)
  }

  @Test fun getStackTraceStringReturnsEmptyStringWithNull() {
    assertThat(Utils.getStackTraceString(null)).isEqualTo("")
  }

  @Test fun getStackTraceStringReturnEmptyStringWithUnknownHostException() {
    assertThat(Utils.getStackTraceString(UnknownHostException())).isEqualTo("")
  }

  @Test fun logLevels() {
    assertThat(Utils.logLevel(Logger.DEBUG)).isEqualTo("DEBUG")
    assertThat(Utils.logLevel(Logger.WARN)).isEqualTo("WARN")
    assertThat(Utils.logLevel(Logger.INFO)).isEqualTo("INFO")
    assertThat(Utils.logLevel(Logger.VERBOSE)).isEqualTo("VERBOSE")
    assertThat(Utils.logLevel(Logger.ASSERT)).isEqualTo("ASSERT")
    assertThat(Utils.logLevel(Logger.ERROR)).isEqualTo("ERROR")
    assertThat(Utils.logLevel(100)).isEqualTo("UNKNOWN")
  }

  @Test fun objectToString() {
    val `object` = "Test"

    assertThat(Utils.toString(`object`)).isEqualTo("Test")
  }

  @Test fun toStringWithNull() {
    assertThat(Utils.toString(null)).isEqualTo("null")
  }

  @Test fun primitiveArrayToString() {
    val booleanArray = booleanArrayOf(true, false, true)
    assertThat(Utils.toString(booleanArray)).isEqualTo("[true, false, true]")

    val byteArray = byteArrayOf(1, 0, 1)
    assertThat(Utils.toString(byteArray)).isEqualTo("[1, 0, 1]")

    val charArray = charArrayOf('a', 'b', 'c')
    assertThat(Utils.toString(charArray)).isEqualTo("[a, b, c]")

    val shortArray = shortArrayOf(1, 3, 5)
    assertThat(Utils.toString(shortArray)).isEqualTo("[1, 3, 5]")

    val intArray = intArrayOf(1, 3, 5)
    assertThat(Utils.toString(intArray)).isEqualTo("[1, 3, 5]")

    val longArray = longArrayOf(1, 3, 5)
    assertThat(Utils.toString(longArray)).isEqualTo("[1, 3, 5]")

    val floatArray = floatArrayOf(1f, 3f, 5f)
    assertThat(Utils.toString(floatArray)).isEqualTo("[1.0, 3.0, 5.0]")

    val doubleArray = doubleArrayOf(1.0, 3.0, 5.0)
    assertThat(Utils.toString(doubleArray)).isEqualTo("[1.0, 3.0, 5.0]")
  }

  @Test fun multiDimensionArrayToString() {
    val `object` = arrayOf(intArrayOf(1, 2, 3), intArrayOf(4, 5, 6))

    assertThat(Utils.toString(`object`)).isEqualTo("[[1, 2, 3], [4, 5, 6]]")
  }
}
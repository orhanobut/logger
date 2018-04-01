package com.orhanobut.logger

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks

class DiskLogAdapterTest {

  @Mock private lateinit var formatStrategy: FormatStrategy

  @Before fun setup() {
    initMocks(this)
  }

  @Test fun isLoggableTrue() {
    val logAdapter = DiskLogAdapter(formatStrategy)

    assertThat(logAdapter.isLoggable(Logger.VERBOSE, "tag")).isTrue()
  }

  @Test fun isLoggableFalse() {
    val logAdapter = object : DiskLogAdapter(formatStrategy) {
      override fun isLoggable(priority: Int, tag: String?): Boolean {
        return false
      }
    }

    assertThat(logAdapter.isLoggable(Logger.VERBOSE, "tag")).isFalse()
  }

  @Test fun log() {
    val logAdapter = DiskLogAdapter(formatStrategy)

    logAdapter.log(Logger.VERBOSE, "tag", "message")

    verify(formatStrategy).log(Logger.VERBOSE, "tag", "message")
  }

}

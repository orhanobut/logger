package com.orhanobut.logger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiskLogAdapterTest {

  @Mock FormatStrategy formatStrategy;

  @Before public void setup() {
    initMocks(this);
  }

  @Test public void isLoggableTrue() throws Exception {
    LogAdapter logAdapter = new DiskLogAdapter(formatStrategy);

    assertThat(logAdapter.isLoggable(Logger.VERBOSE, "tag")).isTrue();
  }

  @Test public void isLoggableFalse() throws Exception {
    LogAdapter logAdapter = new DiskLogAdapter(formatStrategy) {
      @Override public boolean isLoggable(int priority, String tag) {
        return false;
      }
    };

    assertThat(logAdapter.isLoggable(Logger.VERBOSE, "tag")).isFalse();
  }

  @Test public void log() throws Exception {
    LogAdapter logAdapter = new DiskLogAdapter(formatStrategy);

    logAdapter.log(Logger.VERBOSE, "tag", "message");

    verify(formatStrategy).log(Logger.VERBOSE, "tag", "message");
  }

}
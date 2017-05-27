package com.orhanobut.logger;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.orhanobut.logger.Logger.DEBUG;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AndroidLogAdapterTest {

  @Test public void isLoggable() {
    LogAdapter logAdapter = new AndroidLogAdapter();

    assertThat(logAdapter.isLoggable(DEBUG, "tag")).isTrue();
  }

  @Test public void log() {
    FormatStrategy formatStrategy = mock(FormatStrategy.class);
    LogAdapter logAdapter = new AndroidLogAdapter(formatStrategy);

    logAdapter.log(DEBUG, null, "message");

    verify(formatStrategy).log(DEBUG, null, "message");
  }

}
package com.orhanobut.logger;

import android.os.Handler;

import org.junit.Test;

import static com.orhanobut.logger.Logger.DEBUG;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiskLogStrategyTest {

  @Test public void log() {
    Handler handler = mock(Handler.class);
    LogStrategy logStrategy = new DiskLogStrategy(handler);

    logStrategy.log(DEBUG, "tag", "message");

    verify(handler).sendMessage(handler.obtainMessage(DEBUG, "message"));
  }

}
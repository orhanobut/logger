package com.orhanobut.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.orhanobut.logger.Utils.checkNotNull;

public class LogcatLogStrategy implements LogStrategy {

  @Override public void log(int priority, @Nullable String tag, @NonNull String message) {
    checkNotNull(message);

    Log.println(priority, tag, message);
  }

}

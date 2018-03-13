package com.orhanobut.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface LogAdapter {

  boolean isLoggable(int priority, @Nullable String tag);

  void log(int priority, @Nullable String tag, @NonNull String message);
}

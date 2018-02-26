package com.orhanobut.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface LogStrategy {

  void log(int priority, @Nullable String tag, @NonNull String message);
}

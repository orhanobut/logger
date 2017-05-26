package com.orhanobut.sample;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Logger.addAdapter(new AndroidLogAdapter());
    Logger.d("message");

    Logger.clearLogAdapters();
    PrettyFormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .build();

    Logger.addAdapter(new AndroidLogAdapter(formatStrategy));
    Logger.w("no thread info and only 1 method");

    Logger.clearLogAdapters();
    formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .build();

    Logger.addAdapter(new AndroidLogAdapter(formatStrategy));
    Logger.i("no thread info and method info");

    Logger.t("tag").e("Custom tag for only one use");

    Logger.json("{ \"key\": 3, \"value\": something}");

    Logger.d(Arrays.asList("foo", "bar"));

    Map<String, String> map = new HashMap<>();
    map.put("key", "value");
    map.put("key1", "value2");

    Logger.d(map);

    Logger.clearLogAdapters();
    formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .tag("MyTag")
        .build();
    Logger.addAdapter(new AndroidLogAdapter(formatStrategy));

    Logger.w("my log message with my tag");
  }
}

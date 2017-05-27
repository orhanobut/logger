package com.orhanobut.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
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

    Log.d("Tag", "I'm a log which you don't see easily, hehe");
    Log.d("json content", "{ \"key\": 3, \n \"value\": something}");
    Log.d("error", "There is a crash somewhere or any warning");

    Logger.addLogAdapter(new AndroidLogAdapter());
    Logger.d("message");

    Logger.clearLogAdapters();


    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
        .methodCount(0)         // (Optional) How many method line to show. Default 2
        .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
        .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
        .build();

    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    Logger.addLogAdapter(new AndroidLogAdapter() {
      @Override public boolean isLoggable(int priority, String tag) {
        return BuildConfig.DEBUG;
      }
    });

    Logger.addLogAdapter(new DiskLogAdapter());


    Logger.w("no thread info and only 1 method");

    Logger.clearLogAdapters();
    formatStrategy = PrettyFormatStrategy.newBuilder()
        .showThreadInfo(false)
        .methodCount(0)
        .build();

    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
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
    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    Logger.w("my log message with my tag");
  }
}

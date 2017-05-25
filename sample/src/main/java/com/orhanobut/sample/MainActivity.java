package com.orhanobut.sample;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.DiskLogStrategy;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
        .methodCount(2)
        .methodOffset(3)
        .showThreadInfo(true)
        .tag("")
        .build();

    Logger.addAdapter(new AndroidLogAdapter(formatStrategy) {
      @Override public boolean isLoggable(int priority, String tag) {
        return BuildConfig.DEBUG;
      }
    });

    FormatStrategy csvFormatStrategy = CsvFormatStrategy.newBuilder()
        .build();

    Logger.addAdapter(new DiskLogAdapter(csvFormatStrategy){
      @Override public boolean isLoggable(int priority, String tag) {
        return super.isLoggable(priority, tag);
      }
    });

    Logger.d("Test");
  }
}

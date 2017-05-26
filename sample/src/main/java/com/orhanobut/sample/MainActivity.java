package com.orhanobut.sample;

import android.app.Activity;
import android.os.Bundle;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Logger.addAdapter(new AndroidLogAdapter());
    Logger.d("message");
  }
}

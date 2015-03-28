package com.orhanobut.loggersample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.orhanobut.logger.Logger;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.init()
                .setMethodCount(1);

        printNormalLog();
        printPretty();
    }

    void printNormalLog() {
        Log.v(TAG, "hey i am a log which you don't see easily");
        Log.v(TAG, "i = 0 + 1");
        Log.v(TAG, Dummy.JSON_WITH_NO_LINE_BREAK);
        Log.v("test", Dummy.JSON_WITH_LINE_BREAK);
    }

    void printPretty() {
        test2();
        Foo.foo();

        try {
            Class clazz = Class.forName("asdfasd");
        } catch (ClassNotFoundException e) {
            Logger.e(e);
        }

        String test = "[" + Dummy.JSON_WITH_NO_LINE_BREAK + "," + Dummy.JSON_WITH_NO_LINE_BREAK + "]";

        Logger.json(Dummy.SMALL_SON_WITH_NO_LINE_BREAK);

    }

    void test2() {
        Logger.v("test2", 3);
        Logger.v("test3", 0);
        Logger.v("MYTAG", "test3", 2);
        Logger.wtf("test3", 1);
        Logger.d("", "logger with tag");
        Logger.d("tag3", "logger with tag");
        Logger.d("ta", "logger with tag");
    }

}

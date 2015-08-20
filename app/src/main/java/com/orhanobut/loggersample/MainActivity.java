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

        Logger.init("test").hideThreadInfo().setMethodCount(3).setMethodOffset(2).excludeByClassName("MainActivity");

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
            Logger.e(e, "something happened");
        }

        String test = "[" + Dummy.JSON_WITH_NO_LINE_BREAK + "," + Dummy.JSON_WITH_NO_LINE_BREAK + "]";

        Logger.json(Dummy.SMALL_SON_WITH_NO_LINE_BREAK);

        Logger.d("test");

        Logger.t("TEST", 3).d("asdfasdf");
    }

    void test2() {
        Logger.v("test2");
        Logger.v("test3");
        Logger.v("MYTAG");
        Logger.wtf("test3");
        Logger.d("logger with tag");
        Logger.t("tag").d("logger with tag");
        Logger.t("tag", 3).d("logger with 3 method count");
    }

}

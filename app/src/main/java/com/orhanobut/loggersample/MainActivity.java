package com.orhanobut.loggersample;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.init("")
                .smartTag(true)
                .hideThreadInfo() // default shown
                .setMethodCount(2) // default 2
                .setMethodOffset(0) // default 0
                .setMethodOffset(1)
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE); // show log in debug state

        
        Logger.d("test %s%s", "v", 5); // 多参数 可以解决字符拼接的问题
        Logger.d(null);
        printNormalLog();
        printPretty();
    }

    void printNormalLog() {
        Log.d(TAG, "====== Hey i am a log which you don't see easily ======");
        Log.v(TAG, "i = 0 + 1");
        Log.i(TAG, Dummy.JSON_WITH_NO_LINE_BREAK);
        Log.w("test", Dummy.JSON_WITH_LINE_BREAK);
    }

    void printPretty() {
        Logger.d("====== Let's see something interesting ======");
        test2(); // other method
        Foo.foo(); // other class
        User.show();// Internal class
        try {
            Class.forName("asdfasd");
        } catch (ClassNotFoundException e) {
            Logger.e(e, "something happened"); // exception
        }

        String test = "[" + Dummy.JSON_WITH_NO_LINE_BREAK + "," + Dummy.JSON_WITH_NO_LINE_BREAK + "]";

        Logger.json(Dummy.SMALL_SON_WITH_NO_LINE_BREAK); // json

        Logger.t("TEST", 4).d("logger with 4 method count");

        // object
        Logger.object(new User("jack", "f"));

        // list
        List<String> strList = new ArrayList<>();
        strList.add("Android");
        strList.add("ios");
        strList.add("wp");
        Logger.object(strList);

        // array
        Logger.object(new String[]{"Android","ios","wp"});

        // arrays
        double[][] doubles = {
                {1.2, 1.6, 1.7, 30, 33},
                {1.2, 1.6, 1.7, 30, 33},
                {1.2, 1.6, 1.7, 30, 33},
                {1.2, 1.6, 1.7, 30, 33}
        };
        Logger.object(doubles);
    }

    void test2() {
        Logger.v("test %s %d","v",5);
        Logger.wtf("test wtf");
        Logger.t("tag").d("logger with tag");
        Logger.t("tag", 3).d("logger with 3 method count");
    }

    public static class User {
        private String name;
        private String sex;

        User(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        public void log() {
            show();
        }

        public static void show() {
            Logger.d("Internal class");
        }
    }
}

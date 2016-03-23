package com.orhanobut.loggersample;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.initialize(
                Settings.builder()
                        .isSmartTag(true)
                        .showThreadInfo(true)
                        .methodCount(1)
                        .methodOffset(0)
                        .logLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE).build() // show log in debug state
        );

        levTest();
        objTest();
        jsonTest();
        locationTest();
        largeDataTest();
    }

    private void levTest() {
        Logger.v(null);
        Logger.d("%s test", "kale"); // 多参数 可以解决release版本中字符拼接带来的性能消耗
        Logger.t("tag", 3).i("logger with 3 method count");
        try {
            Class.forName("kale");
        } catch (ClassNotFoundException e) {
            Logger.e(e, "something happened"); // exception
        }

        Logger.d("first\nsecond\nthird");
    }

    private void objTest() {
        // object
        Logger.object(new User("jack", "f"));
        // list
        Logger.object(Arrays.asList("kale", "jack", "tony"));
        // array
        Logger.object(new String[]{"Android", "ios", "wp"});
        double[][] doubles = {
                {1.2, 1.6, 1.7, 30, 33},
                {1.2, 1.6, 1.7, 30, 33},
                {1.2, 1.6, 1.7, 30, 33},
                {1.2, 1.6, 1.7, 30, 33}
        };
        Logger.object(doubles);
    }

    private void jsonTest() {
        Logger.json(Dummy.SMALL_SON_WITH_NO_LINE_BREAK); // json
        String j = "[" + Dummy.JSON_WITH_NO_LINE_BREAK + "," + Dummy.JSON_WITH_LINE_BREAK + "]";
        Logger.json(j);
    }

    private void locationTest() {
        Foo.foo(); // other class
        new User("kale", "m").show();// Internal class
    }

    private void largeDataTest() {
        for (int i = 0; i < 20; i++) {
            Logger.d("No." + i);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    public static class User {

        private String name;

        private String sex;

        User(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        public void show() {
            Logger.d("name:%s sex:%s", name, sex);
        }
    }
}

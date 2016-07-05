package com.orhanobut.loggersample;

import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.initialize(
                new Settings()
                        .isShowMethodLink(true)
                        .isShowThreadInfo(false)
                        .setMethodOffset(0)
                        .setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT)
        );

        if (!BuildConfig.DEBUG) {
            // for release
            Logger.plant(new CrashlyticsTree());
        }

        levTest();
        objTest();
        jsonTest();
        locationTest();
        largeDataTest();

        Logger.getSettings().setLogPriority(Log.ASSERT); // close log

        CrashHandler.getInstance().init(); // 崩溃检测处理器
        
//        setRes(123);  // 模拟崩溃
    }

    private void levTest() {
        Logger.v(null);
        Logger.d("%s test", "kale"); // 多参数 可以解决release版本中字符拼接带来的性能消耗
        String test = "abc %s def %s gh";
        Logger.d(test);

        //Logger.d(test, "s"); // Note:incorrect
        Logger.t("Custom Tag");
        Logger.t("Custom Tag").w("logger with custom tag");
        try {
            Class.forName("kale");
        } catch (ClassNotFoundException e) {
            Logger.e(e, "something happened"); // exception
        }

        Logger.d("first\nsecond\nthird");
        test();
    }

    private void test() {
        Logger.d("just test");
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
    // 内部类中打log测试
    ///////////////////////////////////////////////////////////////////////////

    private static class User {

        private String name;

        private String sex;

        User(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }

        void show() {
            Logger.d("name:%s sex:%s", name, sex);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // crash
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 这里模拟后端给客户端传值的情况。
     *
     * 这里的id来自外部输入，如果外部输入的值有问题，那么就可能崩溃。
     * 但理论上是不会有数据异常的，为了不崩溃，这里加try-catch
     */
    private void setRes(@StringRes int resId) {
        TextView view = new TextView(this);

        try {
            view.setText(resId); // 如果出现了崩溃，那么就会调用崩溃处理机制
        } catch (Exception e) {
            // 防御了崩溃
            e.printStackTrace();
            
            // 把崩溃的异常和当前的上下文通过log系统分发
            Logger.e(e, "res id = " + resId);
        }
    }
}

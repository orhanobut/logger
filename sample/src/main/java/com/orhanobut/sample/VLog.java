package com.orhanobut.sample;


import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * xiaofang
 * 19-6-17
 **/
public class VLog {
    public static final String TAG = "VOD";
    public static final String LOG_DIR = "VODLog";
    public static boolean OPEN_LOG = true;

    public static void i(String tag, String msg, Object... format) {
        Logger.i("[" + tag + "] " + msg, format);
    }

    public static void d(String tag, String msg, Object... format) {
        Logger.d("[" + tag + "] " + msg, format);
    }

    public static void w(String tag, String msg, Object... format) {
        Logger.w("[" + tag + "] " + msg, format);
    }

    public static void e(String tag, String msg, Object... format) {
        Logger.e("[" + tag + "] " + msg, format);
    }

    public static void v(String tag, String msg, Object... format) {
        Logger.v("[" + tag + "] " + msg, format);
    }

    public static void json(String msg) {
        Logger.json(msg);
    }

    public static void xml(String msg) {
        Logger.xml(msg);
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    public static void printStackTrace(String tag) {
        Logger.i(tag, getStackTraceString(new Exception()));
    }

    public static void e(String tag, Throwable e) {
        Logger.e(tag, getStackTraceString(new Exception()));
    }
}

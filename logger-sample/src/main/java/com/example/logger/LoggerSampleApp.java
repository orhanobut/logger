package com.example.logger;

import android.app.Application;

import com.orhanobut.logger.*;
import com.orhanobut.logger.LogLevel;

/**
 * Created by jcabotage on 6/28/2016.
 */
public class LoggerSampleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Logger.init().logLevel(LogLevel.FULL);
        } else {
            Logger.init().logLevel(LogLevel.NONE);
        }
    }
}



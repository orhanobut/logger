package com.orhanobut.logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jcabotage on 6/28/2016.
 *
 * <p>Child classes can use parent logging methods that will print pretty logs that include
 * the child class's object hash code, so the lifecycle of specific class instances can be tracked.</p>
 *
 * <p>Child classes can optionally call {@link #setLogLifecycle(boolean)} with true, and
 * system-triggered lifecycle callbacks will be logged.</p>
 */
public abstract class LoggerActivity extends AppCompatActivity {

    int activityHashCode;
    boolean logLifecycle;
    String className;

    /**
     * To log as many lifecycle callbacks as possible, call this method in child class's
     * <code>onCreate()</code> method, BEFORE calling the super method.
     *
     * <p>Logged callbacks chosen based on
     * <a href="https://github.com/xxv/android-lifecycle">Complete Fragment & Activity Lifecycle</a>
     * compiled by Steve Pomeroy. This list focuses on system-triggered callbacks.</p>
     */
    protected final void setLogLifecycle(boolean logLifecycle) {
        this.logLifecycle = logLifecycle;
    }

    protected final boolean isLogLifecycle(){
        return logLifecycle;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHashCode = this.hashCode();
        className = this.getClass().getSimpleName();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onCreate");
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onAttachFragment");
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onContentChanged");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onStart");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onRestart");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onActivityResult");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onRestoreInstanceState");
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onPostCreate");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onResume");
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onPostResume");
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onAttachedToWindow");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onPause");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onSaveInstanceState");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onStop");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (logLifecycle) {
            Logger.h(activityHashCode).lifecycle(className, "onDestroy");
        }
    }

    protected final Printer t(String tag){
        return Logger.t(tag).h(activityHashCode);
    }

    protected final Printer t(int methodCount){
        return Logger.t(methodCount).h(activityHashCode);
    }

    protected final Printer t(String tag, int methodCount){
        return Logger.t(tag, methodCount).h(activityHashCode);
    }

    protected final void d(String message, Object... args){
        Logger.h(activityHashCode).d(message, args);
    }

    protected final void d(Object object){
        Logger.h(activityHashCode).d(object);
    }

    protected final void e(String message, Object... args){
        Logger.h(activityHashCode).e(message, args);
    }

    protected final void e(Throwable throwable, String message, Object... args){
        Logger.h(activityHashCode).e(throwable, message, args);
    }

    protected final void w(String message, Object... args){
        Logger.h(activityHashCode).w(message, args);
    }

    protected final void i(String message, Object... args){
        Logger.h(activityHashCode).i(message, args);
    }

    protected final void v(String message, Object... args){
        Logger.h(activityHashCode).v(message, args);
    }

    protected final void wtf(String message, Object... args){
        Logger.h(activityHashCode).wtf(message, args);
    }

    protected final void json(String json){
        Logger.h(activityHashCode).json(json);
    }

    protected final void xml(String xml){
        Logger.h(activityHashCode).xml(xml);
    }
}

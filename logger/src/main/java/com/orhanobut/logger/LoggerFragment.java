package com.orhanobut.logger;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by jcabotage on 6/28/2016.
 *
 * <p>Child classes can use parent logging methods that will print pretty logs that include
 * the child class's object hash code, so the lifecycle of specific class instances can be tracked.</p>
 *
 * <p>Child classes can optionally call {@link #setLogLifecycle(boolean)} with true, and
 * system-triggered lifecycle callbacks will be logged.</p>
 */
public abstract class LoggerFragment extends Fragment {

    int fragmentHashCode;
    boolean logLifecycle;
    String className;

    /**
     * To log as many lifecycle callbacks as possible, call this method in child class's
     * <code>onAttach()</code> method, BEFORE calling the super method. Note that some callbacks
     * whose super method is never called will not be logged (i.e. <code>onCreateView</code>).
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
    public void onAttach(Context context) {
        super.onAttach(context);

        fragmentHashCode = this.hashCode();
        className = this.getClass().getSimpleName();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onAttach");
        }
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onInflate");
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {

        //note: super method not always called in child classes.

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onCreateAnimation");
        }


        return super.onCreateAnimation(transit, enter, nextAnim);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onCreate");
        }
    }

    //cannot add logs for onCreateView here - the super method of onCreateView is not
    //called in child classes.

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onViewCreated");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onActivityCreated");
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onViewStateRestored");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onStart");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onResume");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onCreateOptionsMenu");
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onPrepareOptionsMenu");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onPause");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onSaveInstanceState");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onStop");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onDestroyView");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onDestroy");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (logLifecycle) {
            Logger.h(fragmentHashCode).lifecycle(className, "onDetach");
        }
    }

    protected final Printer t(String tag){
        return Logger.t(tag).h(fragmentHashCode);
    }

    protected final Printer t(int methodCount){
        return Logger.t(methodCount).h(fragmentHashCode);
    }

    protected final Printer t(String tag, int methodCount){
        return Logger.t(tag, methodCount).h(fragmentHashCode);
    }

    protected final void d(String message, Object... args){
        Logger.h(fragmentHashCode).d(message, args);
    }

    protected final void d(Object object){
        Logger.h(fragmentHashCode).d(object);
    }

    protected final void e(String message, Object... args){
        Logger.h(fragmentHashCode).e(message, args);
    }

    protected final void e(Throwable throwable, String message, Object... args){
        Logger.h(fragmentHashCode).e(throwable, message, args);
    }

    protected final void w(String message, Object... args){
        Logger.h(fragmentHashCode).w(message, args);
    }

    protected final void i(String message, Object... args){
        Logger.h(fragmentHashCode).i(message, args);
    }

    protected final void v(String message, Object... args){
        Logger.h(fragmentHashCode).v(message, args);
    }

    protected final void wtf(String message, Object... args){
        Logger.h(fragmentHashCode).wtf(message, args);
    }

    protected final void json(String json){
        Logger.h(fragmentHashCode).json(json);
    }

    protected final void xml(String xml){
        Logger.h(fragmentHashCode).xml(xml);
    }
}

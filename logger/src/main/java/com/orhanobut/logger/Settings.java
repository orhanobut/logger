package com.orhanobut.logger;

import android.util.Log;

/**
 * @author Kale
 * @date 2016/3/27
 */
public class Settings {

    protected int methodOffset = 0;

    protected boolean showMethodLink = true;

    protected boolean showThreadInfo = false;

    protected int priority = Log.VERBOSE;

    public static Settings getInstance() {
        return new Settings();
    }

    private Settings() {

    }

    public Settings setMethodOffset(int methodOffset) {
        this.methodOffset = methodOffset;
        return this;
    }

    public Settings isShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }

    public Settings isShowMethodLink(boolean showMethodLink) {
        this.showMethodLink = showMethodLink;
        return this;
    }

    public Settings setLogPriority(int priority) {
        this.priority = priority;
        return this;
    }
}

package com.orhanobut.logger;

import android.util.Log;

/**
 * @author Kale
 * @date 2016/3/27
 */
public class Settings {

    int methodOffset = 0;

    boolean showMethodLink = true;

    boolean showThreadInfo = false;

    int priority = Log.VERBOSE;

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

    /**
     * @param priority one of
     *                 {@link Log#VERBOSE},
     *                 {@link Log#DEBUG},
     *                 {@link Log#INFO},
     *                 {@link Log#WARN},
     *                 {@link Log#ERROR}
     */
    public Settings setLogPriority(int priority) {
        this.priority = priority;
        return this;
    }
}

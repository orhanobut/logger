package com.orhanobut.logger;

import android.support.annotation.IntRange;

import lombok.experimental.Builder;

/**
 * @author Orhan Obut
 */
@Builder
public final class Settings {

    protected int methodCount = 1;

    protected boolean showThreadInfo = false;

    protected int methodOffset = 0;

    protected boolean isSmartTag = false;

    protected LogLevel logLevel = LogLevel.FULL;

    public Settings setMethodCount(@IntRange(from = 0) int methodCount) {
        this.methodCount = methodCount < 0 ? 0 : methodCount;
        return this;
    }

}

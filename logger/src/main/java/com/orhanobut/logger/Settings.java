package com.orhanobut.logger;

import android.support.annotation.IntRange;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;

/**
 * @author Orhan Obut
 */
@Builder
public final class Settings {

    @Getter
    private int methodCount = 1;

    @Getter
    @Setter
    private boolean showThreadInfo = false;

    @Getter
    @Setter
    private int methodOffset = 0;

    @Getter
    @Setter
    private boolean isSmartTag = false;

    @Getter
    @Setter
    private LogLevel logLevel = LogLevel.FULL;

    public Settings setMethodCount(@IntRange(from = 0) int methodCount) {
        this.methodCount = methodCount < 0 ? 0 : methodCount;
        return this;
    }

}

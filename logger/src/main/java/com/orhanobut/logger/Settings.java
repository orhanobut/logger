package com.orhanobut.logger;

/**
 * @author Orhan Obut
 */
public final class Settings {

    protected int methodOffset;

    protected boolean showThreadInfo;
    
    public static Settings getInstance() {
        return new Settings();
    }

    public Settings setMethodOffset(int methodOffset) {
        this.methodOffset = methodOffset;
        return this;
    }

    public Settings isShowThreadInfo(boolean showThreadInfo) {
        this.showThreadInfo = showThreadInfo;
        return this;
    }
}

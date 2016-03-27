package com.orhanobut.logger;

import android.support.annotation.NonNull;

import lombok.Getter;
import timber.log.Timber;

public final class LogPrinter extends Timber.DebugTree {

    private static final int STACK_OFFSET = 8;

    private static final String BOTTOM_BORDER = "╚═══════════════════════════";

    public static final String PREFIX_BORDER = "║";

    /**
     * 因为如果设置了tag，那么会在timber中多走一个方法，所以堆栈会发生变化，造成不准确的情况。
     */
    private boolean isCustomTag = true;

    @Getter
    private final Logger.Settings settings;

    private static final String PROPERTY = System.getProperty("line.separator");

    public LogPrinter(Logger.Settings settings) {
        this.settings = settings;
    }

    /**
     * rule for auto tag
     */
    @Override
    protected String createStackElementTag(StackTraceElement ignored) {
        isCustomTag = false;
        int offset = STACK_OFFSET + settings.methodOffset;
        StackTraceElement thisMethodStack = new Throwable().getStackTrace()[offset - 1];
        return super.createStackElementTag(thisMethodStack);
    }

    protected void log(int priority, String tag, @NonNull String message, Throwable ignored) {
        String[] lines = message.split(PROPERTY);
        // 处理有换行的信息
        for (int i = 0, length = lines.length; i < length; i++) {
            if (i == length - 1) {
                printLog(priority, tag, PREFIX_BORDER + lines[i] + getTail());
            } else {
                printLog(priority, tag, PREFIX_BORDER + lines[i]);
            }
        }
        // finally print bottom line
        printLog(priority, tag, BOTTOM_BORDER);

        isCustomTag = true;
    }

    private void printLog(int priority, String tag, String msg) {
        super.log(priority, tag, msg, null);
    }

    /**
     * ==> onCreate(MainActivity.java:827) Thread:main
     */
    private String getTail() {
        if (!settings.showMethodLink) {
            return ""; 
        }
        
        final StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackIndex = STACK_OFFSET + settings.methodOffset + 1;

        if (isCustomTag) {
            stackIndex -= 2;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(" ==> ").append(trace[stackIndex].getMethodName()) // onCreate
                .append("(")
                .append(trace[stackIndex].getFileName()) // MainActivity.java
                .append(":")
                .append(trace[stackIndex].getLineNumber()) // 827
                .append(")");

        if (settings.showThreadInfo) {
            sb.append(" Thread: ").append(Thread.currentThread().getName()); // Thread:main
        }
        return sb.toString();
    }

    /**
     * 根据级别显示log
     * @return 默认所有级别都显示
     */
    @Override
    protected boolean isLoggable(int priority) {
        return true;
    }


    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 3;
    
    
    /*private int getMethodCount() {
        Integer count = LOCAL_METHOD_COUNT.get();
        if (count != null) {
            LOCAL_METHOD_COUNT.remove();
            return count;
        }
        return settings.methodCount;
    }*/
/*
    *//**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     *//*
    @Deprecated
    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LogPrinter.class.getName())
                    && !name.equals(Timber.class.getName())) {
                return --i;
            }
        }
        return -1;
    }*/

}

package com.orhanobut.logger;

import com.orhanobut.logger.util.ObjParser;
import com.orhanobut.logger.util.XmlJsonParser;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import lombok.Getter;

/**
 * Logger is a wrapper of {@link Log}
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
final class LoggerPrinter implements Printer {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 3;

    private static final String BOTTOM_BORDER = "╚═══════════════════════════";


    /**
     * TAG is used for the Log, the name is a little different
     * in order to differentiate the logs easily with the filter
     */
    public static final String TAG = "PRETTYLOGGER";

    /**
     * It is used to determine log settings such as method count, thread info visibility
     */
    @Getter
    private final Settings settings;

    /**
     * Localize single tag and method count for each thread
     */
    private static final ThreadLocal<String> LOCAL_TAG = new ThreadLocal<>();

    private static final ThreadLocal<Integer> LOCAL_METHOD_COUNT = new ThreadLocal<>();

    public LoggerPrinter(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Printer t(String tag, int methodCount) {
        if (tag != null) {
            LOCAL_TAG.set(tag);
        }
        LOCAL_METHOD_COUNT.set(methodCount);
        return this;
    }

    @Override
    public void v(@Nullable String message, Object... args) {
        log(Log.VERBOSE, message, args);
    }

    @Override
    public void d(@Nullable String message, Object... args) {
        log(Log.DEBUG, message, args);
    }

    @Override
    public void i(@Nullable String message, Object... args) {
        log(Log.INFO, message, args);
    }

    @Override
    public void w(String message, Object... args) {
        log(Log.WARN, message, args);
    }

    @Override
    public void e(@Nullable String message, Object... args) {
        e(null, message, args);
    }

    @Override
    public void e(Throwable throwable, @Nullable String message, Object... args) {
        if (throwable != null && message != null) {
            message += " : " + throwable.toString();
        }
        if (throwable != null && message == null) {
            message = throwable.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        log(Log.ERROR, message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    @Override
    public void json(@Nullable String json) {
        d(XmlJsonParser.json(json));
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    @Override
    public void xml(String xml) {
        d(XmlJsonParser.xml(xml));
    }

    /**
     * Only support for Array/Collection/map、
     *
     * @see "https://github.com/pengwei1024/LogUtils"
     */
    @Override
    public void object(@Nullable Object object) {
        d(ObjParser.parseObj(object));
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(int logType, @Nullable String msg, Object... args) {
        if (settings.getLogLevel() == LogLevel.NONE) {
            return;
        }
        String tag = formatTag();
        String message = TextUtils.isEmpty(msg) ? "null" : String.format(msg, args);

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            logContent(logType, tag, message);
            logBottomBorder(logType, tag);
        } else {
            for (int i = 0; i < length; i += CHUNK_SIZE) {
                int count = Math.min(length - i, CHUNK_SIZE);
                //create a new String with system's default charset (which is UTF-8 for Android)
                logContent(logType, tag, new String(bytes, i, count));
            }
            logBottomBorder(logType, tag);
        }
    }

    private void logBottomBorder(int logType, String tag) {
        printLog(logType, tag, BOTTOM_BORDER);
    }

    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (int i = 0, length = lines.length; i < length; i++) {
            if (i == length - 1) {
                printLog(logType, tag, "║" + lines[i] + getTail());
            } else {
                printLog(logType, tag, "║" + lines[i]);
            }
        }
    }

    /**
     * ==> onCreate(MainActivity.java:827) Thread:main
     */
    private String getTail() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();

        int stackOffset = getStackOffset(trace) + settings.getMethodOffset();
        //corresponding method count with the current stack may exceeds the stack trace. Trims the count
        int methodCount = getMethodCount();

        if (methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + stackOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            sb.append(" ==> ").append(trace[stackIndex].getMethodName()) // onCreate
                    .append("(")
                    .append(trace[stackIndex].getFileName()).append(":") // MainActivity.java:
                    .append(trace[stackIndex].getLineNumber()) // 827
                    .append(")");
        }

        if (settings.isShowThreadInfo()) {
            sb.append(" Thread: ").append(Thread.currentThread().getName()); // Thread:main
        }
        return sb.toString();
    }

    private void printLog(int logType, String tag, String msg) {
        switch (logType) {
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ASSERT:
                Log.wtf(tag, msg);
                break;
            case Log.DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(tag, msg);
                break;
        }
    }

    private String getCurrentClassName() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int offset = getStackOffset(trace) + settings.getMethodOffset();
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[offset - 1];
        String result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result;
    }

    /**
     * @return the appropriate tag based on local or global
     */
    private
    @NonNull
    String getTag() {
        String tag = LOCAL_TAG.get();
        if (tag != null) {
            LOCAL_TAG.remove();
            return tag;
        }
        return TAG;
    }

    private String formatTag() {
        String tag = getTag();
        if (settings.isSmartTag()) {
            if (TAG.equals(tag)) {
                return getCurrentClassName();
            } else {
                return getCurrentClassName() + "-" + tag;
            }
        } else {
            return tag;
        }
    }

    private int getMethodCount() {
        Integer count = LOCAL_METHOD_COUNT.get();
        int result = settings.getMethodCount();
        if (count != null) {
            LOCAL_METHOD_COUNT.remove();
            result = count;
        }
        if (result < 0) {
            throw new IllegalStateException("methodCount cannot be negative");
        }
        return result;
    }

    /**
     * Determines the starting index of the stack trace, after method calls made by this class.
     *
     * @param trace the stack trace
     * @return the stack offset
     */
    private int getStackOffset(StackTraceElement[] trace) {
        for (int i = MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (!name.equals(LoggerPrinter.class.getName()) && !name.equals(Logger.class.getName())) {
                return --i;
            }
        }
        return -1;
    }

}

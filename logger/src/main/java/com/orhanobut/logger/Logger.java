package com.orhanobut.logger;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Orhan Obut
 */
public final class Logger {

    /**
     * Android's max limit for a log entry is ~4076 bytes,
     * so 4000 bytes is used as chunk size since default charset
     * is UTF-8
     */
    private static final int CHUNK_SIZE = 4000;
    private static final int JSON_INDENT = 4;
    private static final Settings settings = new Settings();

    private static LogLevel logLevel = LogLevel.FULL;

    /**
     * TAG is used for the Log, the name is a little different
     * in order to differentiate the logs easily with the filter
     */
    private static String TAG = "PRETTYLOGGER";


    //no instance
    private Logger() {
    }

    /**
     * It is used to get the settings object in order to change settings
     *
     * @return the settings object
     */
    public static Settings init() {
        return settings;
    }

    public static Settings init(LogLevel logLevel) {
        return init(logLevel, TAG);
    }

    public static Settings init(String tag) {
        return init(LogLevel.FULL, tag);
    }

    /**
     * It is used to change the tag
     *
     * @param tag is the given string which will be used in Logger
     */
    public static Settings init(LogLevel logLevel, String tag) {
        if (tag == null) {
            throw new NullPointerException("tag may not be null");
        }
        if (tag.trim().length() == 0) {
            throw new IllegalStateException("tag may not be empty");
        }
        Logger.TAG = tag;
        Logger.logLevel = logLevel;
        return settings;
    }

    public static void d(String message) {
        d(message, settings.methodCount);
    }

    public static void d(String message, int methodCount) {
        validateMethodCount(methodCount);
        log(Log.DEBUG, message, methodCount);
    }

    public static void e(String message) {
        e(message, null, settings.methodCount);
    }

    public static void e(Exception e) {
        e(null, e, settings.methodCount);
    }

    public static void e(String message, int methodCount) {
        validateMethodCount(methodCount);
        e(message, null, methodCount);
    }

    public static void e(String message, Exception e) {
        e(message, e, settings.methodCount);
    }

    public static void e(String message, Exception e, int methodCount) {
        validateMethodCount(methodCount);
        if (e != null && message != null) {
            message += " : " + e.toString();
        }
        if (e != null && message == null) {
            message = e.toString();
        }
        if (message == null) {
            message = "No message/exception is set";
        }
        log(Log.ERROR, message, methodCount);
    }

    public static void w(String message) {
        w(message, settings.methodCount);
    }

    public static void w(String message, int methodCount) {
        validateMethodCount(methodCount);
        log(Log.WARN, message, methodCount);
    }

    public static void i(String message) {
        i(message, settings.methodCount);
    }

    public static void i(String message, int methodCount) {
        validateMethodCount(methodCount);
        log(Log.INFO, message, methodCount);
    }

    public static void v(String message) {
        v(message, settings.methodCount);
    }

    public static void v(String message, int methodCount) {
        validateMethodCount(methodCount);
        log(Log.VERBOSE, message, methodCount);
    }

    public static void wtf(String message) {
        wtf(message, settings.methodCount);
    }

    public static void wtf(String message, int methodCount) {
        validateMethodCount(methodCount);
        log(Log.ASSERT, message, methodCount);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        json(json, settings.methodCount);
    }

    /**
     * Formats the json content and print it
     *
     * @param json        the json content
     * @param methodCount number of the method that will be printed
     */
    public static void json(String json, int methodCount) {
        validateMethodCount(methodCount);
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content", methodCount);
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message, methodCount);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message, methodCount);
            }
        } catch (JSONException e) {
            d(e.getCause().getMessage() + "\n" + json, methodCount);
        }
    }

    private static void log(int logType, String message, int methodCount) {
        if (logLevel == LogLevel.NONE) {
            return;
        }
        logHeader(logType);
        logHeaderContent(logType, methodCount);

        //get bytes of message with system's default charset (which is UTF-8 for Android)
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            if (methodCount > 0) {
                logDivider(logType);
            }
            logContent(logType, message);
            logFooter(logType);
            return;
        }
        if (methodCount > 0) {
            logDivider(logType);
        }
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            //create a new String with system's default charset (which is UTF-8 for Android)
            logContent(logType, new String(bytes, i, count));
        }
        logFooter(logType);
    }

    private static void logHeader(int logType) {
        logChunk(logType, "╔════════════════════════════════════════════════════════════════════════════════");
    }

    private static void logHeaderContent(int logType, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if (settings.showThreadInfo) {
            logChunk(logType, "║ Thread: " + Thread.currentThread().getName());
            logDivider(logType);
        }
        String level = "";
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + 5;
            StringBuilder builder = new StringBuilder();
            builder.append("║ ")
                    .append(level)
                    .append(getSimpleClassName(trace[stackIndex].getClassName()))
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            logChunk(logType, builder.toString());
        }
    }

    private static void logFooter(int logType) {
        logChunk(logType, "╚════════════════════════════════════════════════════════════════════════════════");
    }

    private static void logDivider(int logType) {
        logChunk(logType, "╟────────────────────────────────────────────────────────────────────────────────");
    }

    private static void logContent(int logType, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        for (String line : lines) {
            logChunk(logType, "║ " + line);
        }
    }

    private static void logChunk(int logType, String chunk) {
        switch (logType) {
            case Log.ERROR:
                Log.e(TAG, chunk);
                break;
            case Log.INFO:
                Log.i(TAG, chunk);
                break;
            case Log.VERBOSE:
                Log.v(TAG, chunk);
                break;
            case Log.WARN:
                Log.w(TAG, chunk);
                break;
            case Log.ASSERT:
                Log.wtf(TAG, chunk);
                break;
            case Log.DEBUG:
                // Fall through, log debug by default
            default:
                Log.d(TAG, chunk);
                break;
        }
    }

    private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    public static class Settings {
        int methodCount = 2;
        boolean showThreadInfo = true;

        public Settings hideThreadInfo() {
            showThreadInfo = false;
            return this;
        }

        public Settings setMethodCount(int methodCount) {
            validateMethodCount(methodCount);
            this.methodCount = methodCount;
            return this;
        }
    }

    private static void validateMethodCount(int methodCount) {
        if (methodCount < 0 || methodCount > 5) {
            throw new IllegalStateException("methodCount must be bigger than 0 and less than 5");
        }
    }
}

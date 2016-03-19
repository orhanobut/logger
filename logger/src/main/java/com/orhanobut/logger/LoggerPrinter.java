package com.orhanobut.logger;

import com.orhanobut.logger.util.ArrayUtil;
import com.orhanobut.logger.util.ObjectUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 4;

    /**
     * The minimum stack trace index, starts at this class after two native calls.
     */
    private static final int MIN_STACK_OFFSET = 3;

    private static final String BOTTOM_BORDER = "═══════════════════════════";


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

    @Override
    public void w(String message, Object... args) {
        log(Log.WARN, message, args);
    }

    @Override
    public void wtf(String message, Object... args) {
        log(Log.ASSERT, message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    @Override
    public void json(@Nullable String json) {
        if (TextUtils.isEmpty(json)) {
            d("Empty/Null json content.(This msg from logger)");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                d(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                d(message);
            }
        } catch (JSONException e) {
            e(e.getCause().getMessage() + "\n" + json);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    @Override
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("Empty/Null xml content.(This msg from logger)");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.getCause().getMessage() + "\n" + xml);
        }
    }

    /**
     * Only support for Array/Collection/map、
     *
     * @see "https://github.com/pengwei1024/LogUtils"
     */
    @Override
    public void object(@Nullable Object object) {
        if (object == null) {
            d("null");
            return;
        }

        final String simpleName = object.getClass().getSimpleName();
        if (object.getClass().isArray()) {
            StringBuilder msg = new StringBuilder("Temporarily not support more than two dimensional Array!");
            int dim = ArrayUtil.getArrayDimension(object);
            switch (dim) {
                case 1:
                    Pair pair = ArrayUtil.arrayToString(object);
                    msg = new StringBuilder(simpleName.replace("[]", "[" + pair.first + "] {\n"));
                    msg.append(pair.second).append("\n");
                    break;
                case 2:
                    Pair pair1 = ArrayUtil.arrayToObject(object);
                    Pair pair2 = (Pair) pair1.first;
                    msg = new StringBuilder(simpleName.replace("[][]", "[" + pair2.first + "][" + pair2.second + "] {\n"));
                    msg.append(pair1.second).append("\n");
                    break;
                default:
                    break;
            }
            d(msg + "}");
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            String msg = "%s size = %d [\n";
            msg = String.format(Locale.ENGLISH, msg, simpleName, collection.size());
            if (!collection.isEmpty()) {
                Iterator iterator = collection.iterator();
                int flag = 0;
                while (iterator.hasNext()) {
                    String itemString = "[%d]:%s%s";
                    Object item = iterator.next();
                    msg += String.format(Locale.ENGLISH, itemString,
                            flag,
                            ObjectUtil.objectToString(item),
                            flag++ < collection.size() - 1 ? ",\n" : "\n");
                }
            }
            d(msg + "\n]");
        } else if (object instanceof Map) {
            String msg = simpleName + " {\n";
            Map map = (Map) object;
            Set keys = map.keySet();
            for (Object key : keys) {
                String itemString = "[%s -> %s]\n";
                Object value = map.get(key);
                msg += String.format(itemString, ObjectUtil.objectToString(key),
                        ObjectUtil.objectToString(value));
            }
            d(msg + "}");
        } else {
            d(ObjectUtil.objectToString(object));
        }
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(int logType, @Nullable String msg, Object... args) {
        if (settings.getLogLevel() == LogLevel.NONE) {
            return;
        }
        String tag = formatTag();
        String message = createMessage(msg, args);

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
                printLog(logType, tag, lines[i] + getTail());
            } else {
                printLog(logType, tag, lines[i]);
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
                    .append(" (")
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

    private String createMessage(@Nullable String message, Object... args) {
        if (message == null) {
            message = "null";
        }
        return args.length == 0 ? message : String.format(message, args);
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

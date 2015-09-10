package com.orhanobut.logger;

import com.orhanobut.logger.util.ArrayUtil;
import com.orhanobut.logger.util.SystemUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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

  /**
   * It is used to determine log settings such as method count, thread info visibility
   */
  private static final Settings settings = new Settings();

  /**
   * Drawing toolbox
   */
  private static final char TOP_LEFT_CORNER = '╔';
  private static final char BOTTOM_LEFT_CORNER = '╚';
  private static final char MIDDLE_CORNER = '╟';
  private static final char HORIZONTAL_DOUBLE_LINE = '║';
  private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
  private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
  private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

  /**
   * TAG is used for the Log, the name is a little different
   * in order to differentiate the logs easily with the filter
   */
  private static String TAG = "PRETTYLOGGER";

  /**
   * Localize single tag and method count for each thread
   */
  private static final ThreadLocal<String> LOCAL_TAG = new ThreadLocal<>();
  private static final ThreadLocal<Integer> LOCAL_METHOD_COUNT = new ThreadLocal<>();

  /**
   * It is used to change the tag
   *
   * @param tag is the given string which will be used in Logger
   */
  @Override
  public Settings init(String tag) {
    if (tag == null) {
      throw new NullPointerException("tag may not be null");
    }
    if (tag.trim().length() == 0) {
      throw new IllegalStateException("tag may not be empty");
    }
    LoggerPrinter.TAG = tag;
    return settings;
  }

  @Override
  public Settings getSettings() {
    return settings;
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
  public void d(String message, Object... args) {
    log(Log.DEBUG, message, args);
  }

  @Override
  public void e(String message, Object... args) {
    e(null, message, args);
  }

  @Override
  public void e(Throwable throwable, String message, Object... args) {
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
  public void i(String message, Object... args) {
    log(Log.INFO, message, args);
  }

  @Override
  public void v(String message, Object... args) {
    log(Log.VERBOSE, message, args);
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
  public void json(String json) {
    if (TextUtils.isEmpty(json)) {
      d("Empty/Null json content");
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
      d("Empty/Null xml content");
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
   * support list、map、array
   *
   * @see "https://github.com/pengwei1024/LogUtils"
   */
  @Override
  public void object(Object object) {
    if (object != null) {
      final String simpleName = object.getClass().getSimpleName();
      if (object.getClass().isArray()) {
        String msg = "Temporarily not support more than two dimensional Array!";
        int dim = ArrayUtil.getArrayDimension(object);
        switch (dim) {
          case 1:
            Pair pair = ArrayUtil.arrayToString(object);
            msg = simpleName.replace("[]", "[" + pair.first + "] {\n");
            msg += pair.second + "\n";
            break;
          case 2:
            Pair pair1 = ArrayUtil.arrayToObject(object);
            Pair pair2 = (Pair) pair1.first;
            msg = simpleName.replace("[][]", "[" + pair2.first + "][" + pair2.second + "] {\n");
            msg += pair1.second + "\n";
            break;
          default:
            break;
        }
        d(msg + "}");
      } else if (object instanceof Collection) {
        Collection collection = (Collection) object;
        String msg = "%s size = %d [\n";
        msg = String.format(msg, simpleName, collection.size());
        if (!collection.isEmpty()) {
          Iterator<Object> iterator = collection.iterator();
          int flag = 0;
          while (iterator.hasNext()) {
            String itemString = "[%d]:%s%s";
            Object item = iterator.next();
            msg += String.format(itemString, flag, SystemUtil.objectToString(item),
                    flag++ < collection.size() - 1 ? ",\n" : "\n");
          }
        }
        d(msg + "\n]");
      } else if (object instanceof Map) {
        String msg = simpleName + " {\n";
        Map<Object, Object> map = (Map<Object, Object>) object;
        Set<Object> keys = map.keySet();
        for (Object key : keys) {
          String itemString = "[%s -> %s]\n";
          Object value = map.get(key);
          msg += String.format(itemString, SystemUtil.objectToString(key),
                  SystemUtil.objectToString(value));
        }
        d(msg + "}");
      } else {
        d(SystemUtil.objectToString(object));
      }
    } else {
      d(null);
    }
  }

  /**
   * This method is synchronized in order to avoid messy of logs' order.
   */
  private synchronized void log(int logType, String msg, Object... args) {
    if (settings.getLogLevel() == LogLevel.NONE) {
      return;
    }
    String tag = getTag();
    String message = createMessage(msg, args);
    int methodCount = getMethodCount();

    logTopBorder(logType, tag);
    logHeaderContent(logType, tag, methodCount);

    //get bytes of message with system's default charset (which is UTF-8 for Android)
    byte[] bytes = message.getBytes();
    int length = bytes.length;
    if (length <= CHUNK_SIZE) {
      if (methodCount > 0) {
        logDivider(logType, tag);
      }
      logContent(logType, tag, message);
      logBottomBorder(logType, tag);
      return;
    }
    if (methodCount > 0) {
      logDivider(logType, tag);
    }
    for (int i = 0; i < length; i += CHUNK_SIZE) {
      int count = Math.min(length - i, CHUNK_SIZE);
      //create a new String with system's default charset (which is UTF-8 for Android)
      logContent(logType, tag, new String(bytes, i, count));
    }
    logBottomBorder(logType, tag);
  }

  private void logTopBorder(int logType, String tag) {
    logChunk(logType, tag, TOP_BORDER);
  }

  private void logHeaderContent(int logType, String tag, int methodCount) {
    StackTraceElement[] trace = Thread.currentThread().getStackTrace();
    if (settings.isShowThreadInfo()) {
      logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " Thread: " + Thread.currentThread().getName());
      logDivider(logType, tag);
    }
    String level = "";

    int stackOffset = getStackOffset(trace) + settings.getMethodOffset();

    //corresponding method count with the current stack may exceeds the stack trace. Trims the count
    if (methodCount + stackOffset > trace.length) {
      methodCount = trace.length - stackOffset - 1;
    }

    for (int i = methodCount; i > 0; i--) {
      int stackIndex = i + stackOffset;
      if (stackIndex >= trace.length) {
        continue;
      }
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
      logChunk(logType, tag, builder.toString());
    }
  }

  private void logBottomBorder(int logType, String tag) {
    logChunk(logType, tag, BOTTOM_BORDER);
  }

  private void logDivider(int logType, String tag) {
    logChunk(logType, tag, MIDDLE_BORDER);
  }

  private void logContent(int logType, String tag, String chunk) {
    String[] lines = chunk.split(System.getProperty("line.separator"));
    for (String line : lines) {
      logChunk(logType, tag, HORIZONTAL_DOUBLE_LINE + " " + line);
    }
  }

  private void logChunk(int logType, String tag, String chunk) {
    String finalTag = formatTag(tag);
    switch (logType) {
      case Log.ERROR:
        Log.e(finalTag, chunk);
        break;
      case Log.INFO:
        Log.i(finalTag, chunk);
        break;
      case Log.VERBOSE:
        Log.v(finalTag, chunk);
        break;
      case Log.WARN:
        Log.w(finalTag, chunk);
        break;
      case Log.ASSERT:
        Log.wtf(finalTag, chunk);
        break;
      case Log.DEBUG:
        // Fall through, log debug by default
      default:
        Log.d(finalTag, chunk);
        break;
    }
  }

  private String getSimpleClassName(String name) {
    int lastIndex = name.lastIndexOf(".");
    return name.substring(lastIndex + 1);
  }

  private String formatTag(String tag) {
    if (!TextUtils.isEmpty(tag) && !TextUtils.equals(TAG, tag)) {
      return TAG + "-" + tag;
    }
    return TAG;
  }

  /**
   * @return the appropriate tag based on local or global
   */
  private String getTag() {
    String tag = LOCAL_TAG.get();
    if (tag != null) {
      LOCAL_TAG.remove();
      return tag;
    }
    return TAG;
  }

  private String createMessage(String message, Object... args) {
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

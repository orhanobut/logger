package com.orhanobut.logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.orhanobut.logger.Logger.ASSERT;
import static com.orhanobut.logger.Logger.DEBUG;
import static com.orhanobut.logger.Logger.ERROR;
import static com.orhanobut.logger.Logger.INFO;
import static com.orhanobut.logger.Logger.VERBOSE;
import static com.orhanobut.logger.Logger.WARN;

class LoggerPrinter implements Printer {

  /**
   * It is used for json pretty print
   */
  private static final int JSON_INDENT = 2;

  /**
   * Provides one-time used tag for the log message
   */
  private final ThreadLocal<String> localTag = new ThreadLocal<>();

  private final List<LogAdapter> logAdapters = new ArrayList<>();

  @Override public Printer t(String tag) {
    if (tag != null) {
      localTag.set(tag);
    }
    return this;
  }

  @Override public void d(String message, Object... args) {
    log(DEBUG, null, message, args);
  }

  @Override public void d(Object object) {
    log(DEBUG, null, Utils.toString(object));
  }

  @Override public void e(String message, Object... args) {
    e(null, message, args);
  }

  @Override public void e(Throwable throwable, String message, Object... args) {
    log(ERROR, throwable, message, args);
  }

  @Override public void w(String message, Object... args) {
    log(WARN, null, message, args);
  }

  @Override public void i(String message, Object... args) {
    log(INFO, null, message, args);
  }

  @Override public void v(String message, Object... args) {
    log(VERBOSE, null, message, args);
  }

  @Override public void wtf(String message, Object... args) {
    log(ASSERT, null, message, args);
  }

  @Override public void json(String json) {
    if (Utils.isEmpty(json)) {
      d("Empty/Null json content");
      return;
    }
    try {
      json = json.trim();
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
        return;
      }
      e("Invalid Json");
    } catch (JSONException e) {
      e("Invalid Json");
    }
  }

  @Override public void xml(String xml) {
    if (Utils.isEmpty(xml)) {
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
      e("Invalid xml");
    }
  }

  @Override public synchronized void log(int priority, String tag, String message, Throwable throwable) {
    if (throwable != null && message != null) {
      message += " : " + Utils.getStackTraceString(throwable);
    }
    if (throwable != null && message == null) {
      message = Utils.getStackTraceString(throwable);
    }
    if (Utils.isEmpty(message)) {
      message = "Empty/NULL log message";
    }

    for (LogAdapter adapter : logAdapters) {
      if (adapter.isLoggable(priority, tag)) {
        adapter.log(priority, tag, message);
      }
    }
  }

  @Override public void clearLogAdapters() {
    logAdapters.clear();
  }

  @Override public void addAdapter(LogAdapter adapter) {
    logAdapters.add(adapter);
  }

  /**
   * This method is synchronized in order to avoid messy of logs' order.
   */
  private synchronized void log(int priority, Throwable throwable, String msg, Object... args) {
    String tag = getTag();
    String message = createMessage(msg, args);
    log(priority, tag, message, throwable);
  }

  /**
   * @return the appropriate tag based on local or global
   */
  private String getTag() {
    String tag = localTag.get();
    if (tag != null) {
      localTag.remove();
      return tag;
    }
    return null;
  }

  private String createMessage(String message, Object... args) {
    return args == null || args.length == 0 ? message : String.format(message, args);
  }
}
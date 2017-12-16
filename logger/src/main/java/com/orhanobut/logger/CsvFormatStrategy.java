package com.orhanobut.logger;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * CSV formatted file logging for Android.
 * Writes to CSV the following data:
 * epoch timestamp, ISO8601 timestamp (human-readable), log level, tag, log message.
 */
public class CsvFormatStrategy implements FormatStrategy {

  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String NEW_LINE_REPLACEMENT = " <br> ";
  private static final String SEPARATOR = ",";

  private final Date date;
  private final SimpleDateFormat dateFormat;
  private final LogStrategy logStrategy;
  private final String tag;

  private CsvFormatStrategy(Builder builder) {
    date = builder.date;
    dateFormat = builder.dateFormat;
    logStrategy = builder.logStrategy;
    tag = builder.tag;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Override public void log(int priority, String onceOnlyTag, String message) {
    String tag = formatTag(onceOnlyTag);

    date.setTime(System.currentTimeMillis());

    StringBuilder builder = new StringBuilder();

    // machine-readable date/time
    builder.append(Long.toString(date.getTime()));

    // human-readable date/time
    builder.append(SEPARATOR);
    builder.append(dateFormat.format(date));

    // level
    builder.append(SEPARATOR);
    builder.append(Utils.logLevel(priority));

    // tag
    builder.append(SEPARATOR);
    builder.append(tag);

    // message
    if (message.contains(NEW_LINE)) {
      // a new line would break the CSV format, so we replace it here
      message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
    }
    builder.append(SEPARATOR);
    builder.append(message);

    // new line
    builder.append(NEW_LINE);

    logStrategy.log(priority, tag, builder.toString());
  }

  private String formatTag(String tag) {
    if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
      return this.tag + "-" + tag;
    }
    return this.tag;
  }

  public static final class Builder {
    Date date;
    SimpleDateFormat dateFormat;
    LogStrategy logStrategy;
    String tag = "PRETTY_LOGGER";

    private Builder() {
      date = new Date();
      dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
      logStrategy = new DiskLogStrategy.Builder().Build();
    }

    public Builder date(Date val) {
      date = val;
      return this;
    }

    public Builder dateFormat(SimpleDateFormat val) {
      dateFormat = val;
      return this;
    }

    public Builder logStrategy(LogStrategy val) {
      logStrategy = val;
      return this;
    }

    public Builder tag(String tag) {
      this.tag = tag;
      return this;
    }

    public CsvFormatStrategy build() {
      return new CsvFormatStrategy(this);
    }
  }
}

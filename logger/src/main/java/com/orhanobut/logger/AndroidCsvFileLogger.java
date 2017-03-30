package com.orhanobut.logger;


import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * CSV formatted file logging for Android.
 * Writes to CSV the following data:
 * epoch timestamp, ISO8601 timestamp (human-readable), log level, tag, log message.
 */
public class AndroidCsvFileLogger extends AbstractAndroidFileLogger {

  private static final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file
  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String NEW_LINE_REPLACEMENT = " <br> ";
  private static final String SEPARATOR = ",";

  private final Date date;
  private final SimpleDateFormat dateFormat;

  public AndroidCsvFileLogger(String folder) {
    super(MAX_BYTES, folder);
    this.date = new Date();
    this.dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
  }

  @Override protected void writeLog(FileWriter fileWriter, int level, String tag, String message) throws IOException {
    date.setTime(System.currentTimeMillis());

    // machine-readable date/time
    fileWriter.append(Long.toString(date.getTime()));

    // human-readable date/time
    fileWriter.append(SEPARATOR);
    fileWriter.append(dateFormat.format(date));

    // level
    fileWriter.append(SEPARATOR);
    fileWriter.append(LogLevel.toString(level));

    // tag
    fileWriter.append(SEPARATOR);
    fileWriter.append(tag);

    // message
    if (message.contains(NEW_LINE)) {
      // a new line would break the CSV format, so we replace it here
      message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
    }
    fileWriter.append(SEPARATOR);
    fileWriter.append(message);

    // new line
    fileWriter.append(NEW_LINE);
  }
}
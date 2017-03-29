package com.orhanobut.logger;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class AndroidCsvFileLogger implements FileLogger {

  private static final String NEW_LINE = System.getProperty("line.separator");
  private static final String NEW_LINE_REPLACEMENT = " <br> ";
  private static final int MAX_LINES = 2048;
  private static final String SEPARATOR = ",";

  private final int logLevel;
  private final Handler handler;

  AndroidCsvFileLogger(int logLevel, String folder) {
    this.logLevel = logLevel;
    HandlerThread ht = new HandlerThread("FileLogger." + folder);
    ht.start();
    handler = new WriteHandler(ht.getLooper(), folder);
  }

  @Override public void log(int level, String tag, String message) {
    if (level >= logLevel) {
      // do nothing on the calling thead, simply pass the tag/msg to the background thread
      handler.sendMessage(handler.obtainMessage(level, new String[]{tag, message}));
    }
  }

  //region helper to generate file names
  @SuppressWarnings("checkstyle:emptyblock")
  private static File getLogFile(String folderName, String fileName) {

    File folder = new File(folderName);
    if (!folder.exists()) {
      folder.mkdirs();
    }

    int newFileCount = 0;
    File newFile;
    File existingFile = null;

    newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
    while (newFile.exists()) {
      existingFile = newFile;
      newFileCount++;
      newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
    }

    if (existingFile != null) {
      try {
        int lineCount = 0;
        BufferedReader br = new BufferedReader(new FileReader(existingFile));
        while (br.readLine() != null) {
          lineCount++;
        }

        if (lineCount >= MAX_LINES) {
          return newFile;
        } else {
          return existingFile;
        }
      } catch (IOException e) { /* fail silently */ }
    }

    return newFile;
  }
  //endregion

  //region handler to write to disk on background thread
  private static class WriteHandler extends Handler {

    private final String folder;
    private final Date date;
    private final SimpleDateFormat dateFormat;

    private WriteHandler(Looper looper, String folder) {
      super(looper);
      this.date = new Date();
      this.dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.UK);
      this.folder = folder;
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override public void handleMessage(Message msg) {

      String[] data = (String[]) msg.obj;

      FileWriter fileWriter = null;
      File logFile = getLogFile(folder, "logs");

      try {
        fileWriter = new FileWriter(logFile, true);

        date.setTime(System.currentTimeMillis());

        // machine-readable date/time
        fileWriter.append(Long.toString(date.getTime()));

        // human-readable date/time
        fileWriter.append(SEPARATOR);
        fileWriter.append(dateFormat.format(date));

        // level
        fileWriter.append(SEPARATOR);
        fileWriter.append(LogLevel.toString(msg.what));

        // tag
        fileWriter.append(SEPARATOR);
        fileWriter.append(data[0]);

        // message
        String message;
        if (data[1].contains(NEW_LINE)) {
          // a new line would break the CSV format, so we replace it here
          message = data[1].replace(NEW_LINE, NEW_LINE_REPLACEMENT);
        } else {
          message = data[1];
        }
        fileWriter.append(SEPARATOR);
        fileWriter.append(message);

        // new line
        fileWriter.append(NEW_LINE);

        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        if (fileWriter != null) {
          try {
            fileWriter.flush();
            fileWriter.close();
          } catch (IOException e1) { /* fail silently */ }
        }
      }
    }
  }
  //endregion
}

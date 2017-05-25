package com.orhanobut.logger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Abstract class that takes care of background threading the file log operation on Android.
 * implementing classes are free to directly perform I/O operations there.
 */
public class DiskLogStrategy implements LogStrategy {

  private final Handler handler;
  private final int maxFileSize;
  private final String folder;

  /**
   * Default constructor.
   *
   * @param maxFileSize max size of a file before breaking it into a new log file
   * @param folder      folder to create the log files.
   */
  public DiskLogStrategy(String folder, int maxFileSize) {
    this.maxFileSize = maxFileSize;
    this.folder = folder;
    HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
    ht.start();
    handler = new WriteHandler(ht.getLooper());
  }

  @Override public void log(int level, String tag, String message) {
    // do nothing on the calling thread, simply pass the tag/msg to the background thread
    handler.sendMessage(handler.obtainMessage(level, message));
  }

  private File getLogFile(String folderName, String fileName) {

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
      if (existingFile.length() >= maxFileSize) {
        return newFile;
      } else {
        return existingFile;
      }
    }

    return newFile;
  }

  private class WriteHandler extends Handler {

    private WriteHandler(Looper looper) {
      super(looper);
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override public void handleMessage(Message msg) {
      String content = (String) msg.obj;

      FileWriter fileWriter = null;
      File logFile = getLogFile(folder, "logs");

      try {
        fileWriter = new FileWriter(logFile, true);

        writeLog(fileWriter, content);

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

    /**
     * This is always called on a single background thread.
     * Implementing classes must ONLY write to the fileWriter and nothing more.
     * The abstract class takes care of everything else including close the stream and catching IOException
     *
     * @param fileWriter an instance of FileWriter already initialised to the correct file
     */
    private void writeLog(FileWriter fileWriter, String content) throws IOException {
      fileWriter.append(content);
    }
  }
}
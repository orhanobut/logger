package com.orhanobut.logger;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.orhanobut.logger.Utils.checkNotNull;

/**
 * Abstract class that takes care of background threading the file log operation on Android.
 * implementing classes are free to directly perform I/O operations there.
 *
 * Writes all logs to the disk with CSV format.
 */
public class DiskLogStrategy implements LogStrategy {

  @NonNull private final Handler handler;

  @NonNull
  public static Builder newBuilder() {
    return new Builder();
  }

  public DiskLogStrategy(@NonNull Handler handler) {
    this.handler = checkNotNull(handler);
  }

  @Override public void log(int level, @Nullable String tag, @NonNull String message) {
    checkNotNull(message);

    // do nothing on the calling thread, simply pass the tag/msg to the background thread
    handler.sendMessage(handler.obtainMessage(level, message));
  }

  static class WriteHandler extends Handler {

    @NonNull private final String folder;
    private final int maxFileSize;

    WriteHandler(@NonNull Looper looper, @NonNull String folder, int maxFileSize) {
      super(checkNotNull(looper));
      this.folder = checkNotNull(folder);
      this.maxFileSize = maxFileSize;
    }

    @SuppressWarnings("checkstyle:emptyblock")
    @Override public void handleMessage(@NonNull Message msg) {
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
    private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content) throws IOException {
      checkNotNull(fileWriter);
      checkNotNull(content);

      fileWriter.append(content);
    }

    private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
      checkNotNull(folderName);
      checkNotNull(fileName);

      File folder = new File(folderName);
      if (!folder.exists()) {
        //TODO: What if folder is not created, what happens then?
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
        }
        return existingFile;
      }

      return newFile;
    }
  }

  public static final class Builder {
    private static final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file

    String directory;
    HandlerThread ht;
    Handler handler;

    private Builder() {
    }

    /**
     * Set the log destination directory
     * The default destination will be used if not supplied
     */
    @NonNull public Builder directory(@NonNull String directory) {
      this.directory = directory;
      return this;
    }

    /**
     * Set the {@link HandlerThread} which handles this strategy
     * A default {@link HandlerThread} will be created if not supplied
     */
    @NonNull public Builder handlerThread(@NonNull HandlerThread ht) {
      this.ht = ht;
      return this;
    }

    /**
     * Set the {@link Handler}
     * A default {@link Handler} will be created if not supplied
     */
    @NonNull public Builder handler(@NonNull Handler handler) {
      this.handler = handler;
      return this;
    }

    /**
     * Create the {@link DiskLogStrategy} given the configuration
     */
    @NonNull public DiskLogStrategy build() {
      if (directory == null) {
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        directory = diskPath + File.separatorChar + "logger";
      }
      if (ht == null) {
        ht = new HandlerThread("AndroidFileLogger." + directory);
        ht.start();
      }
      if (handler == null) {
        handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), directory, MAX_BYTES);
      }
      return new DiskLogStrategy(handler);
    }
  }
}

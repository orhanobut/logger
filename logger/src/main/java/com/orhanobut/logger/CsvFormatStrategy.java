package com.orhanobut.logger;

public class CsvFormatStrategy implements FormatStrategy {
  @Override public void log(int priority, String tag, String message) {

  }

  /**
   * Returns the instance of FileLogger, creating one if necessary.
   * The created logger is a CSV logger and points to the folder "logger" on external storage
   * If developer wants to use internal folder, he/she must call {@link #fileLogger(FileLogger)}
   *
   * @return file logger
   */
//  FileLogger getFileLogger() {
//    if (fileLogger == null) {
//      fileLogger = new AndroidCsvFileLogger(
//          Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "logger");
//    }
//    return fileLogger;
//  }
}

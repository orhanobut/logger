package com.orhanobut.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Provides convenient methods to some common operations
 */
final class Utils {

  private Utils() {
    // Hidden constructor.
  }

  /**
   * Returns true if the string is null or 0-length.
   *
   * @param str the string to be examined
   * @return true if str is null or zero length
   */
  static boolean isEmpty(CharSequence str) {
    return str == null || str.length() == 0;
  }

  /**
   * Returns true if a and b are equal, including if they are both null.
   * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
   * both the arguments were instances of String.</i></p>
   *
   * @param a first CharSequence to check
   * @param b second CharSequence to check
   * @return true if a and b are equal
   * <p>
   * NOTE: Logic slightly change due to strict policy on CI -
   * "Inner assignments should be avoided"
   */
  static boolean equals(CharSequence a, CharSequence b) {
    if (a == b) return true;
    if (a != null && b != null) {
      int length = a.length();
      if (length == b.length()) {
        if (a instanceof String && b instanceof String) {
          return a.equals(b);
        } else {
          for (int i = 0; i < length; i++) {
            if (a.charAt(i) != b.charAt(i)) return false;
          }
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
   * in unit tests.
   *
   * @return Stack trace in form of String
   */
  static String getStackTraceString(Throwable tr) {
    if (tr == null) {
      return "";
    }

    // This is to reduce the amount of log spew that apps do in the non-error
    // condition of the network being unavailable.
    Throwable t = tr;
    while (t != null) {
      if (t instanceof UnknownHostException) {
        return "";
      }
      t = t.getCause();
    }

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    tr.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }

  /** Return whether a log should be logged */
  static boolean isLoggable(int logLevel, int priority) {
    return priority >= logLevel;
  }

}

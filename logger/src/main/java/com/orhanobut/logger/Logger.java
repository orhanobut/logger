package com.orhanobut.logger;

/**
 * Logger is a wrapper of {@link android.util.Log}
 * But more pretty, simple and powerful
 *
 * @author Orhan Obut
 */
public final class Logger {

    private static LogPrinter printer;

    // @formatter:off
    @Deprecated private Logger() {}
    // @formatter:on

    public static void initialize(Settings settings) {
        printer = new LogPrinter(settings);
    }

    public static Settings getSettings() {
        return printer.getSettings();
    }

    public static LogPrinter t(String tag) {
        return printer.t(tag, printer.getSettings().methodCount);
    }

    public static LogPrinter t(int methodCount) {
        return printer.t(null, methodCount);
    }

    public static LogPrinter t(String tag, int methodCount) {
        return printer.t(tag, methodCount);
    }

    public static void v(String message, Object... args) {
        printer.v(message, args);
    }

    public static void d(String message, Object... args) {
        printer.d(message, args);
    }

    public static void i(String message, Object... args) {
        printer.i(message, args);
    }

    public static void w(String message, Object... args) {
        printer.w(message, args);
    }

    public static void e(String message, Object... args) {
        printer.e(null, message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        printer.e(throwable, message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        printer.json(json);
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        printer.xml(xml);
    }

    /**
     * Formats the json content and print it
     *
     * @param object Bean,Array,Collection,Map and so on
     */
    public static void object(Object object) {
        printer.object(object);
    }

}

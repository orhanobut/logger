package com.orhanobut.logger;

import com.orhanobut.logger.util.ObjParser;
import com.orhanobut.logger.util.XmlJsonParser;

import timber.log.Timber;

/**
 * Logger is a wrapper of {@link Timber}
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
        Timber.plant(printer);
    }

    public static Settings getSettings() {
        return printer.getSettings();
    }

    public static Timber.Tree t(String tag) {
        return Timber.tag(tag);
    }

    public static void v(String message, Object... args) {
        Timber.v(message, args);
    }

    public static void d(String message, Object... args) {
        Timber.d(message, args);
    }

    public static void i(String message, Object... args) {
        Timber.i(message, args);
    }

    public static void w(String message, Object... args) {
        Timber.w(message, args);
    }

    public static void w(Throwable throwable, String message, Object... args) {
        Timber.w(throwable, message, args);
    }

    public static void e(String message, Object... args) {
        Timber.e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Timber.e(throwable, message, args);
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        Timber.d(XmlJsonParser.json(json));
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        Timber.d(XmlJsonParser.xml(xml));
    }

    /**
     * Formats the json content and print it
     *
     * @param object Bean,Array,Collection,Map,Pojo and so on
     */
    public static void object(Object object) {
        Timber.d(ObjParser.parseObj(object));
    }

    public static void plant(Timber.Tree tree) {
        Timber.plant(tree);
    }
    
    public static void uprootAll() {
        Timber.uprootAll();
    }

}

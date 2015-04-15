package com.orhanobut.logger;

/**
 * @author Orhan Obut
 */
public interface Printer {

    Printer t(String tag, int methodCount);

    Settings init(String tag);

    void d(String message, Object... args);

    void d(Object object, Object... args);

    void e(String message, Object... args);

    void e(Throwable throwable, String message, Object... args);

    void w(String message, Object... args);

    void i(String message, Object... args);

    void v(String message, Object... args);

    void wtf(String message, Object... args);

    void json(String json);

    void xml(String xml);
}

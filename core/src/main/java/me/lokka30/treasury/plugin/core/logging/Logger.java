package me.lokka30.treasury.plugin.core.logging;

public interface Logger {

    void info(String message);

    void warn(String message);

    void error(String message);

    void error(String message, Throwable t);
}

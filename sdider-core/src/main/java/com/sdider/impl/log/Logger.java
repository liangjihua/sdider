package com.sdider.impl.log;

import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 *
 * @author yujiaxin
 */
public class Logger {
    private volatile org.slf4j.Logger logger;
    private String name;
    private Class<?> clazz;

    private Logger(String name) {
        this.name = name;
    }

    private Logger(Class<?> clazz) {
        this.clazz = clazz;
    }

    public static Logger getInstance(String name) {
        return new Logger(name);
    }

    public static Logger getInstance(Class<?> clazz) {
        return new Logger(clazz);
    }

    private void ensureLogger() {
        if (logger == null) {
            if (name != null) {
                logger = LoggerFactory.getLogger(name);
            } else {
                logger = LoggerFactory.getLogger(clazz);
            }
        }
    }

    public String getName() {
        ensureLogger();
        return logger.getName();
    }

    public boolean isTraceEnabled() {
        ensureLogger();
        return logger.isTraceEnabled();
    }

    public void trace(String msg) {
        ensureLogger();
        logger.trace(msg);
    }

    public void trace(String format, Object arg) {
        ensureLogger();
        logger.trace(format, arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.trace(format, arg1, arg2);
    }

    public void trace(String format, Object... arguments) {
        ensureLogger();
        logger.trace(format, arguments);
    }

    public void trace(String msg, Throwable t) {
        ensureLogger();
        logger.trace(msg, t);
    }

    public boolean isTraceEnabled(Marker marker) {
        ensureLogger();
        return logger.isTraceEnabled(marker);
    }

    public void trace(Marker marker, String msg) {
        ensureLogger();
        logger.trace(marker, msg);
    }

    public void trace(Marker marker, String format, Object arg) {
        ensureLogger();
        logger.trace(marker, format, arg);
    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.trace(marker, format, arg1, arg2);
    }

    public void trace(Marker marker, String format, Object... argArray) {
        ensureLogger();
        logger.trace(marker, format, argArray);
    }

    public void trace(Marker marker, String msg, Throwable t) {
        ensureLogger();
        logger.trace(marker, msg, t);
    }

    public boolean isDebugEnabled() {
        ensureLogger();
        return logger.isDebugEnabled();
    }

    public void debug(String msg) {
        ensureLogger();
        logger.debug(msg);
    }

    public void debug(String format, Object arg) {
        ensureLogger();
        logger.debug(format, arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.debug(format, arg1, arg2);
    }

    public void debug(String format, Object... arguments) {
        ensureLogger();
        logger.debug(format, arguments);
    }

    public void debug(String msg, Throwable t) {
        ensureLogger();
        logger.debug(msg, t);
    }

    public boolean isDebugEnabled(Marker marker) {
        ensureLogger();
        return logger.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String msg) {
        ensureLogger();
        logger.debug(marker, msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        ensureLogger();
        logger.debug(marker, format, arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.debug(marker, format, arg1, arg2);
    }

    public void debug(Marker marker, String format, Object... arguments) {
        ensureLogger();
        logger.debug(marker, format, arguments);
    }

    public void debug(Marker marker, String msg, Throwable t) {
        ensureLogger();
        logger.debug(marker, msg, t);
    }

    public boolean isInfoEnabled() {
        ensureLogger();
        return logger.isInfoEnabled();
    }

    public void info(String msg) {
        ensureLogger();
        logger.info(msg);
    }

    public void info(String format, Object arg) {
        ensureLogger();
        logger.info(format, arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.info(format, arg1, arg2);
    }

    public void info(String format, Object... arguments) {
        ensureLogger();
        logger.info(format, arguments);
    }

    public void info(String msg, Throwable t) {
        ensureLogger();
        logger.info(msg, t);
    }

    public boolean isInfoEnabled(Marker marker) {
        ensureLogger();
        return logger.isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg) {
        ensureLogger();
        logger.info(marker, msg);
    }

    public void info(Marker marker, String format, Object arg) {
        ensureLogger();
        logger.info(marker, format, arg);
    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.info(marker, format, arg1, arg2);
    }

    public void info(Marker marker, String format, Object... arguments) {
        ensureLogger();
        logger.info(marker, format, arguments);
    }

    public void info(Marker marker, String msg, Throwable t) {
        ensureLogger();
        logger.info(marker, msg, t);
    }

    public boolean isWarnEnabled() {
        ensureLogger();
        return logger.isWarnEnabled();
    }

    public void warn(String msg) {
        ensureLogger();
        logger.warn(msg);
    }

    public void warn(String format, Object arg) {
        ensureLogger();
        logger.warn(format, arg);
    }

    public void warn(String format, Object... arguments) {
        ensureLogger();
        logger.warn(format, arguments);
    }

    public void warn(String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.warn(format, arg1, arg2);
    }

    public void warn(String msg, Throwable t) {
        ensureLogger();
        logger.warn(msg, t);
    }

    public boolean isWarnEnabled(Marker marker) {
        ensureLogger();
        return logger.isWarnEnabled(marker);
    }

    public void warn(Marker marker, String msg) {
        ensureLogger();
        logger.warn(marker, msg);
    }

    public void warn(Marker marker, String format, Object arg) {
        ensureLogger();
        logger.warn(marker, format, arg);
    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.warn(marker, format, arg1, arg2);
    }

    public void warn(Marker marker, String format, Object... arguments) {
        ensureLogger();
        logger.warn(marker, format, arguments);
    }

    public void warn(Marker marker, String msg, Throwable t) {
        ensureLogger();
        logger.warn(marker, msg, t);
    }

    public boolean isErrorEnabled() {
        ensureLogger();
        return logger.isErrorEnabled();
    }

    public void error(String msg) {
        ensureLogger();
        logger.error(msg);
    }

    public void error(String format, Object arg) {
        ensureLogger();
        logger.error(format, arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.error(format, arg1, arg2);
    }

    public void error(String format, Object... arguments) {
        ensureLogger();
        logger.error(format, arguments);
    }

    public void error(String msg, Throwable t) {
        ensureLogger();
        logger.error(msg, t);
    }

    public boolean isErrorEnabled(Marker marker) {
        ensureLogger();
        return logger.isErrorEnabled(marker);
    }

    public void error(Marker marker, String msg) {
        ensureLogger();
        logger.error(marker, msg);
    }

    public void error(Marker marker, String format, Object arg) {
        ensureLogger();
        logger.error(marker, format, arg);
    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {
        ensureLogger();
        logger.error(marker, format, arg1, arg2);
    }

    public void error(Marker marker, String format, Object... arguments) {
        ensureLogger();
        logger.error(marker, format, arguments);
    }

    public void error(Marker marker, String msg, Throwable t) {
        ensureLogger();
        logger.error(marker, msg, t);
    }
}

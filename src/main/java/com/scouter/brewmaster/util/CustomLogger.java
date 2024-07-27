package com.scouter.brewmaster.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class CustomLogger {

    private static final Logger LOGGER = LogUtils.getLogger();
    private final String source;
    private final String className;

    // Constructor with source only
    public CustomLogger(String source) {
        this.source = source;
        this.className = null;
    }

    // Constructor with source and class name
    public CustomLogger(String source, String className) {
        this.source = source;
        this.className = className;
    }

    // Method to log an info message
    public void logInfo(String message) {
        LOGGER.info(formatMessage(message));
    }

    // Method to log a formatted info message
    public void logInfo(String message, Object... args) {
        LOGGER.info(formatMessage(message), args);
    }

    // Method to log a warning message
    public void logWarning(String message) {
        LOGGER.warn(formatMessage(message));
    }

    // Method to log a formatted warning message
    public void logWarning(String message, Object... args) {
        LOGGER.warn(formatMessage(message), args);
    }

    // Method to log an error message
    public void logError(String message) {
        LOGGER.error(formatMessage(message));
    }

    // Method to log a formatted error message
    public void logError(String message, Object... args) {
        LOGGER.error(formatMessage(message), args);
    }

    // Method to log an error message with an exception
    public void logError(String message, Throwable throwable) {
        LOGGER.error(formatMessage(message), throwable);
    }

    // Method to log a debug message
    public void logDebug(String message) {
        LOGGER.debug(formatMessage(message));
    }

    // Method to log a formatted debug message
    public void logDebug(String message, Object... args) {
        LOGGER.debug(formatMessage(message), args);
    }

    // Method to log a trace message
    public void logTrace(String message) {
        LOGGER.trace(formatMessage(message));
    }

    // Method to log a formatted trace message
    public void logTrace(String message, Object... args) {
        LOGGER.trace(formatMessage(message), args);
    }

    // Method to log a message with a custom level
    public void logCustom(Level level, String message) {
        switch (level) {
            case TRACE:
                logTrace(message);
                break;
            case DEBUG:
                logDebug(message);
                break;
            case INFO:
                logInfo(message);
                break;
            case WARN:
                logWarning(message);
                break;
            case ERROR:
                logError(message);
                break;
            default:
                throw new IllegalArgumentException("Unsupported log level: " + level);
        }
    }

    // Method to log a formatted message with a custom level
    public void logCustom(Level level, String message, Object... args) {
        switch (level) {
            case TRACE:
                logTrace(message, args);
                break;
            case DEBUG:
                logDebug(message, args);
                break;
            case INFO:
                logInfo(message, args);
                break;
            case WARN:
                logWarning(message, args);
                break;
            case ERROR:
                logError(message, args);
                break;
            default:
                throw new IllegalArgumentException("Unsupported log level: " + level);
        }
    }

    // Method to log a performance metric
    public void logPerformance(String metric, long timeInMillis) {
        LOGGER.info(formatMessage("Performance Metric: {} - {} ms", metric, timeInMillis));
    }

    // Method to log an important event
    public void logEvent(String eventName, String details) {
        LOGGER.info(formatMessage("Event: {} - {}", eventName, details));
    }

    // Method to log the current state of an object
    public void logState(String objectName, Object state) {
        LOGGER.info(formatMessage("State of {}: {}", objectName, state));
    }

    // Method to format the message with the source and optionally the class name
    private String formatMessage(String message, Object... args) {
        if (className != null) {
            return String.format("[%s - %s] %s", source, className, message);
        } else {
            return String.format("[%s] %s", source, message);
        }
    }

    // Enum for custom log levels
    public enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}

/*
 * This file is/was part of Treasury. To read more information about Treasury such as its licensing, see <https://github.com/ArcanePlugins/Treasury>.
 */

package me.lokka30.treasury.plugin.core.logging;

/**
 * Represents a logger interface, which is to be implemented by platforms.
 *
 * @author MrIvanPlays
 * @since v1.0.0
 */
public interface Logger {

    /**
     * Logs an informative message. Can use legacy minecraft colors.
     *
     * @param message the message you want to log.
     */
    void info(String message);

    /**
     * Logs a warning message. Can use legacy minecraft colors.
     *
     * @param message the message you want to log.
     */
    void warn(String message);

    /**
     * Logs an error message. Can use legacy minecraft colors.
     *
     * @param message the message you want to log.
     */
    void error(String message);

    /**
     * Logs an error message with a {@link Throwable} appended. Can use legacy minecraft colors.
     *
     * @param message the message you want to log.
     * @param t       the exception to append to the message.
     */
    void error(String message, Throwable t);

}

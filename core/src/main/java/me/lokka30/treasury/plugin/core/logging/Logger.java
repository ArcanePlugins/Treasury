/*
 * Copyright (c) 2021 lokka30.
 *
 * This code is part of Treasury, an Economy API for Minecraft servers. Please see <https://github.com/lokka30/Treasury> for more information on this resource.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

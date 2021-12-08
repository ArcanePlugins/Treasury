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

package me.lokka30.treasury.plugin.core.utils;

/**
 * This is a small class useful for timing simple things such as the time required to start-up a plugin or run a command.
 * <p>
 * Mark the starting point of the timer with `QuickTimer timer = new QuickTimer()`, then get the time (in milliseconds)
 * since it started using `QuickTimer#getTimer()`.
 *
 * @author lokka30
 * @see System#currentTimeMillis()
 * @since v1.0.0
 */
@SuppressWarnings("unused")
public class QuickTimer {

    private long startTime;

    public QuickTimer() {
        start();
    }

    public QuickTimer(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Re/start the timer.
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     * @return time (millis) since start time
     */
    public long getTimer() {
        return System.currentTimeMillis() - startTime;
    }
}

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Joe Burnard
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.sponges.dubtrack4j.util;

import io.sponges.dubtrack4j.internal.DubtrackAPIImpl;

public final class Logger {

    // TODO replace this with slf4j or java's logger

    private Logger() {
    }

    public enum LoggingMode {
        WARNING, NORMAL, DEBUG
    }

    public static void debug(String msg) {
        if (DubtrackAPIImpl.getLoggingMode() == LoggingMode.DEBUG) {
            System.out.println("DEBUG> " + msg);
        }
    }

    public static void info(String msg) {
        if (DubtrackAPIImpl.getLoggingMode() != LoggingMode.WARNING) {
            System.out.println("INFO> " + msg);
        }
    }

    public static void warning(String msg) {
        System.out.println("WARNING> " + msg);
    }

}

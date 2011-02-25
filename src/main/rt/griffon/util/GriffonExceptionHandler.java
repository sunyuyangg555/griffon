/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.util;

import org.codehaus.groovy.runtime.StackTraceUtils;

/**
 * Catches and sanitizes all uncaught exceptions.
 *
 * @author Danno Ferrin
 */
public class GriffonExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final String GRIFFON_FULL_STACKTRACE = "griffon.full.stacktrace";
    private final boolean fullStacktrace;

    public GriffonExceptionHandler() {
        fullStacktrace = Boolean.getBoolean(GRIFFON_FULL_STACKTRACE);
    }

    public void uncaughtException(Thread t, Throwable e) {
        handle(e);
    }

    public void handle(Throwable throwable) {
        try {
            if(!fullStacktrace) StackTraceUtils.deepSanitize(throwable);
            throwable.printStackTrace(System.err);
        } catch (Throwable t) {
            // don't let the exception get thrown out, will cause infinite looping!
        }
    }

    public static void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(new GriffonExceptionHandler());
        System.setProperty("sun.awt.exception.handler", GriffonExceptionHandler.class.getName());
    }
}
/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.model.logging;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * Provides delayed logging using the {@link DefaultLogger}.
 * 
 * This class is useful if your class has a cyclic dependency on the
 * DefaultLogger, but you need to log things in your constructor.
 * <p>
 * Example:
 * 
 * <pre>
 * public DependentClass() { 
 *     //this class depends on DefaultLogger and 
 *     //DefaultLogger depends on this class
 *     logger = new LogQueue(DependentClass.class);
 *     logger.log(Level.DEBUG_INT, 
 *              &quot;Creating new instance of DependentClass&quot;);
 *     initThings();
 *     ...
 *     
 *     logger.makeReady();
 * }
 * </pre>
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class LogQueue {

    /**
     * A simple record that holds the properties of a delayed log entry.
     */
    private class LogEntry {
        public int level;
        public Object message;
        public Throwable t;

        /**
         * Create a new log entry.
         * 
         * @param level
         *            log level (Level.xxx_INT)
         * @param message
         *            message to log
         * @param t
         *            optional throwable to log (can be <code>null</code>)
         */
        public LogEntry(int level, Object message, Throwable t) {
            this.level = level;
            this.message = message;
            this.t = t;
        }
    }

    /**
     * Queue for delayed logging.
     */
    private Queue<LogEntry> logQueue = null;

    /**
     * Internal logger to which the delayed messages are flushed.
     */
    private Logger logger = null;

    /**
     * Name of the logger (one it's initialized)
     */
    private String loggerName = null;

    /**
     * Creates a new LogQueue that uses the given string as the logger's name.
     * 
     * @param loggerName
     *            name of the logger.
     */
    public LogQueue(String loggerName) {
        this.loggerName = loggerName;
        this.logQueue = new LinkedList<LogEntry>();
    }

    /**
     * Creates a new LogQueue that uses the name of the given class as the
     * logger's name.
     * 
     * @param clazz
     *            class to use as the logger's name.
     */
    public LogQueue(Class<?> clazz) {
        this(clazz.getName());
    }

    /**
     * Writes the given log message to the internal logger (assuming it has been
     * initialized)
     * 
     * @param level
     *            log level
     * @param message
     *            message to log (such as a string)
     * @param t
     *            optional throwable to log, can be null
     */
    private void writeToLogger(int level, Object message, Throwable t) {
        switch (level) {
        case Priority.DEBUG_INT:
            logger.debug(message, t);
            break;

        case Priority.ERROR_INT:
            logger.error(message, t);
            break;

        case Priority.FATAL_INT:
            logger.fatal(message, t);
            break;

        case Priority.INFO_INT:
            logger.info(message, t);
            break;

        case Level.TRACE_INT:
            logger.trace(message, t);
            break;

        case Priority.WARN_INT:
            logger.warn(message, t);
            break;

        default:
            // unknown or invalid log level (including ALL_INT and OFF_INT) - do
            // not log
            break;
        }
    }

    /**
     * Uses the default logger to log the given message. If the logger hasn't
     * been initialized, yet, the message is put into a queue and will be
     * written to the logger as soon as it's been initialized.
     * 
     * @param level
     *            log level
     * @param message
     *            message to log (such as a string)
     * @param t
     *            optional throwable to log, can be null
     */
    public void log(int level, Object message, Throwable t) {
        if (isReady()) {
            // use this opportunity to flush any queued messages
            LogEntry queued = logQueue.poll();
            while (queued != null) {
                writeToLogger(queued.level, queued.message, queued.t);
                queued = logQueue.poll();
            }
            writeToLogger(level, message, t);
        } else {
            // logger hasn't been initialized, yet
            logQueue.add(new LogEntry(level, message, t));
        }
    }

    /**
     * Uses the default logger to log the given message. If the logger hasn't
     * been initialized, yet, the message is put into a queue and will be
     * written to the logger as soon as it's been initialized.
     * 
     * @param level
     *            log level
     * @param message
     *            message to log (such as a string)
     */
    public void log(int level, Object message) {
        log(level, message, null);
    }

    /**
     * @return whether the internal logger has been initialized by calling
     *         makeReady.
     */
    public boolean isReady() {
        return logger != null;
    }

    /**
     * Initializes the internal logger. Any queued log messages will be flushed
     * at the next opportunity, but not in this method.
     */
    public void makeReady() {
        if (logger == null) {
            logger = DefaultLogger.getLogger(loggerName);
        }
    }
}

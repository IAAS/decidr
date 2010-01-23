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
 *     logger.log(Level.DEBUG, 
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
        public Level level;
        public Object message;
        public Throwable t;

        /**
         * Create a new log entry.
         * 
         * @param level
         *            log level
         * @param message
         *            message to log
         * @param t
         *            optional throwable to log (can be <code>null</code>)
         */
        public LogEntry(Level level, Object message, Throwable t) {
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
     * Whether the logging system should be initialized at the next opportunity
     */
    private boolean initializeAsap = false;

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
     * Initializes the logging system and flushes any log messages.
     */
    private void graspLogOpportunity() {
        if (initializeAsap && !isReady() && DefaultLogger.isInitialized()) {
            initializeAsap = false;
            logger = DefaultLogger.getLogger(loggerName);
        }

        if (isReady()) {
            // use this opportunity to flush any queued messages
            LogEntry queued = logQueue.poll();
            while (queued != null) {
                writeToLogger(queued.level, queued.message, queued.t);
                queued = logQueue.poll();
            }
        }
    }

    /**
     * @return whether the internal logger has been initialized by calling
     *         makeReady.
     */
    public boolean isReady() {
        return logger != null;
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
    public void log(Level level, Object message) {
        log(level, message, null);
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
    public void log(Level level, Object message, Throwable t) {
        graspLogOpportunity();
        if (isReady()) {
            writeToLogger(level, message, t);
        } else {
            // logger hasn't been initialized, yet
            logQueue.add(new LogEntry(level, message, t));
        }
    }

    /**
     * At the next opportunity (but not in this method), the logger will be
     * initialized and any queued log messages will be flushed.
     */
    public void makeReady() {
        if (!isReady()) {
            initializeAsap = true;
        }
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
    private void writeToLogger(Level level, Object message, Throwable t) {
        logger.log(level, message, t);
    }
}

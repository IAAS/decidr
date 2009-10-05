package de.decidr.model.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Provides a common logging configuration for the DecidR components and
 * subsystems.
 * <p>
 * XXX use JDBC appender instead of or in addition to console appender, add
 * support for remotely triggered log level changes. See
 * http://www.dankomannhaupt.de/projects/index.html for an improved JDBC
 * appender.
 */
public class DefaultLogger {

    private static Appender defaultAppender = new ConsoleAppender(new PatternLayout("[%p] %m%n"),
                ConsoleAppender.SYSTEM_OUT);

    static {
        // run configurator - every new logger will inherit the defaultAppender
         BasicConfigurator.configure(defaultAppender);
        // set default logging options
        Logger.getLogger("de.decidr").setLevel(Level.DEBUG);
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
}
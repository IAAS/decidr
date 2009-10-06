package de.decidr.model.logging;

import java.util.Enumeration;

import org.apache.log4j.Appender;
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
@SuppressWarnings("unchecked")
public class DefaultLogger {

    private static final String DEFAULT_LOGGER = "de.decidr";
    private static Appender defaultAppender = new ConsoleAppender(
            new PatternLayout("[%p] %m%n"), ConsoleAppender.SYSTEM_OUT);

    static {
        // run configurator - every new logger will inherit the defaultAppender
        // RR remove if unnecessary
        // BasicConfigurator.resetConfiguration();
        // BasicConfigurator.configure(defaultAppender);

        boolean configured = false;
        Appender current;
        Enumeration<Appender> appenders = Logger.getLogger(DEFAULT_LOGGER)
                .getAllAppenders();
        while (appenders.hasMoreElements()) {
            current = appenders.nextElement();
            if (current.equals(defaultAppender)
                    || current instanceof ConsoleAppender) {
                configured = true;
            }
        }

        if (!configured) {
            Logger.getLogger(DEFAULT_LOGGER).addAppender(defaultAppender);
            // set default logging options
            Logger.getLogger(DEFAULT_LOGGER).setLevel(Level.DEBUG);
            // Don't pass messages to higher-level loggers
            Logger.getLogger(DEFAULT_LOGGER).setAdditivity(false);
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
}
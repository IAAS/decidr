package de.decidr.model.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * TODO replace stubs with DecidR centralized logging. Add documentation.
 */
public class DefaultLogger {
    private static Appender app = new ConsoleAppender(new PatternLayout(
            "[%p] %m%n"), ConsoleAppender.SYSTEM_OUT);

    static {
        // run some configurator
        BasicConfigurator.configure(app);

        // set default logging options
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Logger.getRootLogger().addAppender(app);
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
}
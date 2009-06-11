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

    private static boolean configured = false;
    private static Appender app;

    private static void configure() {
        app = new ConsoleAppender(new PatternLayout("[%p] %m%n"),
                ConsoleAppender.SYSTEM_OUT);

        // run some configurator
        BasicConfigurator.configure(app);

        // set default logging options
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Logger.getRootLogger().addAppender(app);

        // don't run this method redundantly
        configured = true;
    }

    public static Logger getLogger(Class<?> clazz) {
        if (!configured) {
            configure();
        }
        Logger.getLogger(clazz).addAppender(app);
        return Logger.getLogger(clazz);
    }

    public static Logger getLogger(String name) {
        if (!configured) {
            configure();
        }
        Logger.getLogger(name).addAppender(app);
        return Logger.getLogger(name);
    }
}
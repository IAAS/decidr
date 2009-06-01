package de.decidr.model.logging;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
 * TODO replace stubs with DecidR centralized logging. Add documentation.
 */
public class DefaultLogger {

    private static class Factory implements LoggerFactory {

        @Override
        public Logger makeNewLoggerInstance(String name) {
            Logger result = LogManager.exists(name);
            if (result == null) {
                result = LogManager.getLogger(name);
                result.addAppender(new ConsoleAppender());
            }

            return result;
        }
    }

    private static Factory factory = new Factory();

    public static Logger getLogger(Class clazz) {
        return LogManager.getLogger(clazz.getCanonicalName(), factory);
    }

    public static Logger getLogger(String name) {
        return LogManager.getLogger(name, factory);
    }

}
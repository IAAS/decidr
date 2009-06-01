package de.decidr.model.logging;

import org.apache.log4j.Logger;

/**
 * TODO replace stubs with DecidR centralized logging. Add documentation.
 */
public class DefaultLogger {

    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }

    @SuppressWarnings("unchecked")
    public static Logger getLogger(Class clazz) {
        return Logger.getLogger(clazz);
    }
}
package org.example.catering.cateringserviceapp.helpers;




import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class AppLogger {

    private AppLogger() {

    }

    private static Logger getLogger() {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();

        return LoggerFactory.getLogger(className);
    }

    public static void info(String message, Object... args) {
        getLogger().info(message, args);
    }

    public static void warn(String message, Object... args) {
        getLogger().warn(message, args);
    }

    public static void error(String message, Object... args) {
        getLogger().error(message, args);
    }

    public static void error(String message, Throwable t) {
        getLogger().error(message, t);
    }

}

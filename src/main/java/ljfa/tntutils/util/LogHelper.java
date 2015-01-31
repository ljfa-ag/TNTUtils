package ljfa.tntutils.util;

import ljfa.tntutils.Reference;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;

public class LogHelper {
    public static void log(Level logLevel, String format, Object... data) {
        FMLLog.log(Reference.MODNAME, logLevel, format, data);
    }
    
    public static void log(Level logLevel, Throwable ex, String format, Object... data) {
        FMLLog.log(Reference.MODNAME, logLevel, ex, format, data);
    }
    
    public static void trace(String format, Object... data) {
        log(Level.TRACE, format, data);
    }
    
    public static void debug(String format, Object... data) {
        log(Level.DEBUG, format, data);
    }
    
    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }
    
    public static void warn(String format, Object... data) {
        log(Level.WARN, format, data);
    }
    
    public static void error(String format, Object... data) {
        log(Level.ERROR, format, data);
    }
    
    public static void fatal(String format, Object... data) {
        log(Level.FATAL, format, data);
    }
}
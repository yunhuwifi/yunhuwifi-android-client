
package com.yunhuwifi.vcard;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private static final Logger logger = Logger.getLogger(Log.class.getName());

   
    public static final int VERBOSE = 2;

    
    public static final int DEBUG = 3;

 
    public static final int INFO = 4;

  
    public static final int WARN = 5;

   
    public static final int ERROR = 6;

 
    public static final int ASSERT = 7;

    private Log() {
    }

    
    public static int v(String tag, String msg) {
        return println(VERBOSE, tag, msg);
    }

    
    public static int v(String tag, String msg, Throwable tr) {
        return println(VERBOSE, tag, msg + '\n' + getStackTraceString(tr));
    }

   
    public static int d(String tag, String msg) {
        return println(DEBUG, tag, msg);
    }

    
    public static int d(String tag, String msg, Throwable tr) {
        return println(DEBUG, tag, msg + '\n' + getStackTraceString(tr));
    }

    
    public static int i(String tag, String msg) {
        return println(INFO, tag, msg);
    }

  
    public static int i(String tag, String msg, Throwable tr) {
        return println(INFO, tag, msg + '\n' + getStackTraceString(tr));
    }

    
    public static int w(String tag, String msg) {
        return println(WARN, tag, msg);
    }

   
    public static int w(String tag, String msg, Throwable tr) {
        return println(WARN, tag, msg + '\n' + getStackTraceString(tr));
    }

   
    public static boolean isLoggable(String tag, int level) {
        return true;
    }
        
   
    public static int w(String tag, Throwable tr) {
        return println(WARN, tag, getStackTraceString(tr));
    }

  
    public static int e(String tag, String msg) {
        return println(ERROR, tag, msg);
    }

    
    public static int e(String tag, String msg, Throwable tr) {
        int r = println(ERROR, tag, msg + '\n' + getStackTraceString(tr));
        return r;
    }

    
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    
    public static int println(int priority, String tag, String msg) {
        logger.logp(prioToLevel(priority), tag, null, msg);
        return 1;
    }

    private static Level prioToLevel(int priority) {
        switch (priority) {
            case ASSERT: return Level.ALL;
            case DEBUG: return Level.FINEST;
            case ERROR: return Level.SEVERE;
            case INFO: return Level.INFO;
            case VERBOSE: return Level.ALL;
            case WARN: return Level.WARNING;
        }
        return Level.WARNING;
    }
}

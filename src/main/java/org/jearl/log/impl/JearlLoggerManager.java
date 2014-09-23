/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log.impl;

import org.jearl.log.level.Audit;
import org.jearl.log.util.ApplicationUtils;
import java.lang.reflect.Method;
import java.net.URL;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.xml.DOMConfigurator;
import org.jearl.log.CustomLogField;
import org.jearl.log.ErrorCategory;
import org.jearl.log.JearlLogger;

/**
 *
 * @author can
 */
public class JearlLoggerManager implements JearlLogger {

    static String APP_NAME = "N/A";
    static String FQCN = JearlLoggerManager.class.getName();
    static boolean JDK14 = false;
    static int STACKTRACE_LIMIT = 10;
    static URL url = Thread.currentThread().getContextClassLoader().getResource("jearllogger.xml");

    static {
        String version = System.getProperty("java.version");
        if (version != null) {
            JDK14 = version.startsWith("1.4");  
        }
    }
    private Logger logger;

    public JearlLoggerManager(String className) {
        DOMConfigurator.configure(url);
        this.logger = Logger.getLogger(className);
    }

    public JearlLoggerManager(Class clazz, String applicationName) {
        DOMConfigurator.configure(url);
        this.logger = Logger.getLogger(clazz.getName());
        setAppName(applicationName);
    }

    public Logger getLogger() {
        return logger;
    }

    private void setAppName(String appName) {
        if (appName != null) {
            APP_NAME = appName;
        }
    }

    public static void putMDC(CustomLogField logField, Object value) {
        if (value != null) {
            MDC.put(logField.getValue(), value);
        }
    }

    public StringBuilder getPrintedStackTrace(Throwable t, int limit) {
        StringBuilder builder = new StringBuilder();
        if (t != null) {
            StackTraceElement[] traceElements = t.getStackTrace();
            if (traceElements != null) {
                int iterationCount = traceElements.length;
                if (iterationCount > limit) {
                    iterationCount = limit;
                }
                for (int i = 0; i < iterationCount; i++) {
                    builder.append("at ");
                    builder.append("---");
                    builder.append(traceElements[i].getClassName());
                    builder.append(".");
                    builder.append(traceElements[i].getMethodName());
                    builder.append(".");
                    builder.append("(");
                    builder.append(traceElements[i].getFileName());
                    builder.append(traceElements[i].getLineNumber());
                    builder.append(")");
                    builder.append("\n");
                }
            }
        }
        return builder;
    }

    public void fillAllMDC() {
        putMDC(CustomLogField.LOG_APPLICATION, APP_NAME);
    }

    @Override
    public void fillAllMDC(ErrorCategory category) {
        putMDC(CustomLogField.LOG_APPLICATION, APP_NAME);
        putMDC(CustomLogField.ERROR_CATEGORY, category.getValue());
    }

    @Override
    public void fillAllMDC(Integer userid, ErrorCategory category) {
        putMDC(CustomLogField.LOG_APPLICATION, APP_NAME);
        putMDC(CustomLogField.ERROR_CATEGORY, category.getValue());
        putMDC(CustomLogField.LOG_USER, userid.toString());
    }

    public void fillAllMDC(ErrorCategory category, Throwable t) {
        putMDC(CustomLogField.LOG_APPLICATION, APP_NAME);
        putMDC(CustomLogField.ERROR_CATEGORY, category.getValue());
        if (t != null) {
            putMDC(CustomLogField.TEMP_STACKTRACE, getReadyErrorCauseMessage(t, STACKTRACE_LIMIT));
        }
    }

    @Override
    public void fillAllMDC(Integer userid, ErrorCategory category, Throwable t) {
        putMDC(CustomLogField.LOG_APPLICATION, APP_NAME);
        putMDC(CustomLogField.ERROR_CATEGORY, category.getValue());
        putMDC(CustomLogField.LOG_USER, userid.toString());

        if (t != null) {
            putMDC(CustomLogField.TEMP_STACKTRACE, getReadyErrorCauseMessage(t, STACKTRACE_LIMIT));
        }
    }

    public String getReadyErrorCauseMessage(Throwable t, int limit) {
        if (t != null) {
            String msg = "";
            if (t.getMessage() != null) {
                msg = t.toString();
            }
            msg += "\n";
            msg += getPrintedStackTrace(t, limit);
            return msg;
        } else {
            return null;
        }
    }

    @Override
    public void trace(Object msg) {
        fillAllMDC();
        logger.log(FQCN, Level.TRACE, msg, null);
        MDC.clear();
    }

    @Override
    public void trace(Object msg, Throwable t) {
        logger.log(FQCN, Level.TRACE, msg, t);
        logNestedException(Level.TRACE, msg, t);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isEnabledFor(Level.TRACE);
    }

    @Override
    public void audit(Object msg) {
        fillAllMDC();
        logger.log(FQCN, Audit.AUDIT, msg, null);
        MDC.clear();
    }

    @Override
    public void debug(Object msg) {
        logger.log(FQCN, Level.DEBUG, msg, null);
    }

    @Override
    public void debug(Object msg, Throwable t) {
        logger.log(FQCN, Level.DEBUG, msg, t);
        logNestedException(Level.DEBUG, msg, t);
    }

    @Override
    public void debug(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        fillAllMDC(userid, category, t);
        logger.log(FQCN, Level.WARN, msg, t);
        logNestedException(Level.WARN, msg, t);
        MDC.clear();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void info(Object msg) {
        logger.log(FQCN, Level.INFO, msg, null);
    }

    @Override
    public void info(Object msg, Throwable t) {
        logger.log(FQCN, Level.INFO, msg, t);
        logNestedException(Level.INFO, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void warn(Object msg) {
        logger.log(FQCN, Level.WARN, msg, null);
    }

    @Override
    public void warn(Object msg, Throwable t) {
        logger.log(FQCN, Level.WARN, msg, t);
        logNestedException(Level.WARN, msg, t);
    }

    @Override
    public void warn(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        fillAllMDC(userid, category, t);
        logger.log(FQCN, Level.WARN, msg, t);
        logNestedException(Level.WARN, msg, t);
        MDC.clear();
    }

    @Override
    public void error(Object msg, ErrorCategory category) {
        fillAllMDC(category);
        logger.log(FQCN, Level.ERROR, msg, null);
        MDC.clear();
    }

    @Override
    public void error(Object msg, ErrorCategory category, Throwable t) {
        fillAllMDC(category,t);
        logger.log(FQCN, Level.ERROR, msg, t);
        logNestedException(Level.ERROR, msg, t);
        MDC.clear();
    }

    @Override
    public void error(Integer userid, ErrorCategory category, Object msg) {
        fillAllMDC(userid, category);
        logger.log(FQCN, Level.ERROR, msg, null);
        logNestedException(Level.ERROR, msg, null);
        MDC.clear();
    }

    @Override
    public void error(Integer userid, ErrorCategory category, Throwable t) {
        fillAllMDC(userid, category, t);
        logger.log(FQCN, Level.ERROR, null, null);
        logNestedException(Level.ERROR, null, null);
        MDC.clear();
    }

    @Override
    public void error(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        fillAllMDC(userid, category, t);
        logger.log(FQCN, Level.ERROR, msg, t);
        logNestedException(Level.ERROR, msg, t);
        MDC.clear();
    }

    @Override
    public void fatal(Object msg) {
        logger.log(FQCN, Level.FATAL, msg, null);
    }

    @Override
    public void fatal(Object msg, Throwable t) {
        logger.log(FQCN, Level.FATAL, msg, t);
        logNestedException(Level.FATAL, msg, t);
    }

    @Override
    public void fatal(ErrorCategory category, Object msg, Throwable t) {
        fillAllMDC(category, t);
        logger.log(FQCN, Level.FATAL, msg, t);
        logNestedException(Level.FATAL, msg, t);
    }

    /**
     * @param userid 
     * developer has to set user id value in session to learn who faces related issue
     * @param category 
     * @see ErrorCategory
     * developer has to set category @see ErrorCategory issue category
     * @param msg dynamic message
     * @param t Throwable object 
     * 
     */
    @Override
    public void fatal(Integer userid, ErrorCategory category, Object msg, Throwable t) {
        fillAllMDC(userid, category, t);
        logger.log(FQCN, Level.FATAL, msg, t);
        logNestedException(Level.FATAL, msg, t);
        MDC.clear();
    }

    @Override
    public void logNestedException(Level level, Object msg, Throwable t) {
        if (t == null) {
            return;
        }
        try {
            Class tC = t.getClass();
            Method mA[] = tC.getMethods();
            Method nextThrowableMethod = null;
            for (int i = 0; i < mA.length; i++) {
                if (("getCause".equals(mA[i].getName()) && !JDK14)
                        || "getRootCause".equals(mA[i].getName())
                        || "getNextException".equals(mA[i].getName())
                        || "getException".equals(mA[i].getName())) {
// check param types
                    Class params[] = mA[i].getParameterTypes();
                    if (params == null || params.length == 0) {

                        nextThrowableMethod = mA[i];
                        break;
                    }
                }
            }
            if (nextThrowableMethod != null) {
                Throwable next = (Throwable) nextThrowableMethod.invoke(t, new Object[0]);
                if (next != null) {
                    this.logger.log(FQCN, level, "Previous log CONTINUED", next);
                    putMDC(CustomLogField.TEMP_STACKTRACE, next);
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void changeLevelTreshold(Level l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

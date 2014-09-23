/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log;

import org.apache.log4j.Level;

/**
 *
 * @author can
 */
public interface JearlLogger {

    public void changeLevelTreshold(Level l);

    public void fillAllMDC(ErrorCategory category);

    public void fillAllMDC(Integer userid, ErrorCategory category);

    public void fillAllMDC(Integer userid, ErrorCategory category, Throwable t);

    public void audit(Object msg);

    public void trace(Object msg);

    public void trace(Object msg, Throwable t);

    public boolean isTraceEnabled();

    public void debug(Object msg);

    public void debug(Object msg, Throwable t);

    public void debug(Integer userid, ErrorCategory category, Object msg, Throwable t);

    public boolean isDebugEnabled();

    public void info(Object msg);

    public void info(Object msg, Throwable t);

    public boolean isInfoEnabled();

    public void warn(Object msg);

    public void warn(Object msg, Throwable t);

    public void warn(Integer userid, ErrorCategory category, Object msg, Throwable t);

    public void error(Object msg, ErrorCategory category);

    public void error(Object msg, ErrorCategory category, Throwable t);

    public void error(Integer userid, ErrorCategory category, Throwable t);

    public void error(Integer userid, ErrorCategory category, Object msg);

    public void error(Integer userid, ErrorCategory category, Object msg, Throwable t);

    public void fatal(Object msg);

    public void fatal(ErrorCategory category, Object msg, Throwable t);

    public void fatal(Object msg, Throwable t);

    public void fatal(Integer userid, ErrorCategory category, Object msg, Throwable t);

    public void logNestedException(Level level, Object msg, Throwable t);
}

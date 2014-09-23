/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log.filter;

import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.jearl.log.level.Audit;

/**
 *
 * @author can
 */
public class AuditLevelFilter extends Filter {
    
    @Override
    public int decide(LoggingEvent le) {
        if (le.getLevel().equals(le.getLevel().equals(Level.TRACE))) {
            return Filter.DENY;
        }
        return 0;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log.level;

import org.apache.log4j.Level;

/**
 *
 * @author can
 */
public class Audit extends Level {

    static public final int AUDIT_INT = Level.TRACE_INT - 1;
    private static String AUDIT_STR = "AUDIT";
    public static final Audit AUDIT = new Audit(AUDIT_INT, AUDIT_STR, 8);

    public Audit(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    public static Level toLevel(String sArg) {
        return (Level) toLevel(sArg, Audit.AUDIT);
    }

    public static Level toLevel(String sArg, Level defaultValue) {
        if (sArg == null) {
            return defaultValue;
        }
        String stringVal = sArg.toUpperCase();
        if (stringVal.equals(AUDIT_STR)) {
            return Audit.AUDIT;
        }
        return Level.toLevel(sArg, (Level) defaultValue);
    }

    public static Level toLevel(int i)
            throws IllegalArgumentException {
        if (i == AUDIT_INT) {
            return Audit.AUDIT;
        } else {
            return Level.toLevel(i);
        }
    }
}

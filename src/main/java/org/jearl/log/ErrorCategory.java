/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log;

/**
 *
 * @author can
 */
public enum ErrorCategory {

    SYSTEM_ERROR(100),
    DATABASE_ERROR(101),
    EMAIL_ERROR(102),
    USER_ERROR(103),
    ACCESS_ERROR(104),
    NULL_PARAMETER_ERROR(105),
    WRONG_PARAMETER_ERROR(106),
    LOGIN_ERROR(107);
    private int value;

    private ErrorCategory(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

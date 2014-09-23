/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jearl.log.util;

/**
 *
 * @author can
 */
public class ApplicationUtils {
    /**
     * 
     * BU METHODU KULLANABILMEK ICIN PROJE ICINDEN CALISMAK GEREKIYOR
     * AKSI TAKDIRDE SUNUCUYA YUKLENMIS BIR WEB UYGULAMASINDA BU METOD DOMAIN1 SEKLINDE BIR STRING VERECEKTIR !!!!
     * Using System.getProperty(...) take current application name
     * String dir = System.getProperty("user.dir") Eg. for Linux '\', for Windows '/'
     */
    public static String getCurrentApplicationName() {
        String dir = System.getProperty("user.dir");
        return dir.substring(dir.lastIndexOf(System.getProperty("file.separator")) + 1);
    }
}

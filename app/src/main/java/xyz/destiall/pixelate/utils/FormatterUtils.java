package xyz.destiall.pixelate.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Written By Yong Hong
 */

public class FormatterUtils {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("MMM d',' yyyy HH:mm:ss z");

    /**
     * Helps return a beautified string of time.
     * @param epocTime - Usually derived from System.currentTimeMillis()
     * @param timeZone - EST, UTC...
     * @return Returns an error if Epoc is negative or zero.
     */
    public static String formatTime(Long epocTime, String timeZone) {
        if (epocTime > 0) {
            FORMAT.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date date = new Date(epocTime);
            return FORMAT.format(date);
        }
        return StringUtils.INVALID_TIME_FORMAT;
    }
}

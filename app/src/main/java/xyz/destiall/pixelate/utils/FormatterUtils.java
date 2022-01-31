package xyz.destiall.pixelate.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FormatterUtils {

    /**
     * Helps return a beautified string of time.
     * @param epocTime - Usually derived from System.currentTimeMillis()
     * @param timeZone - EST, UTC...
     * @return Returns an error if Epoc is negative or zero.
     */
    public static String formatTime(Long epocTime, String timeZone)
    {
        if(epocTime > 0)
        {
            SimpleDateFormat formatter= new SimpleDateFormat("MMM d',' yyyy HH:mm:ss z");
            formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date date = new Date(epocTime);

            return formatter.format(date);
        }
        return "INVALID TIME FORMAT";
    }
}

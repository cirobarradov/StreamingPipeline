package com.ferrovial.digitalhub;

import org.joda.time.Instant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

    public static String getTimestamp(Instant instant)
    {
        TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);

        return df.format(instant.toDateTime().toDate());
    }

    public static String getTimestamp()
    {
        TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        Calendar calendar = Calendar.getInstance();
        //calendar.add(Calendar.HOUR_OF_DAY, -2);
        Date date = calendar.getTime();
        return df.format(date);
    }

}

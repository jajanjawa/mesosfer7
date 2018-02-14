package com.github.jajanjawa.mesosfer7.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class DateFormat  {
    public static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final DateFormat INSTANCE = new DateFormat();
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat serverDateFormat;

    public static DateFormat getInstance() {
        return INSTANCE;
    }

    private DateFormat() {
        dateFormat = new SimpleDateFormat(PATTERN, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());

        serverDateFormat = new SimpleDateFormat(PATTERN, Locale.US);
        serverDateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
    }

    public Date parseServer(String source) {
        try {
            return  serverDateFormat.parse(source);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date parse(String source) {
        try {
            return dateFormat.parse(source);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String format(Date date) {
        return dateFormat.format(date);
    }

    public String formatServer(Date date) {
        return serverDateFormat.format(date);
    }
}

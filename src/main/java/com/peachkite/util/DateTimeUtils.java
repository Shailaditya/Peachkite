package com.peachkite.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static Date parseDateString(String dateString,String dateFormat) throws java.text.ParseException{
        DateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.parse(dateString);
    }
}
